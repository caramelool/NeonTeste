package br.com.caramelo.neonteste.ui.home

import android.arch.lifecycle.Observer
import br.com.caramelo.neonteste.data.model.Me
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.data.repository.TokenManager
import br.com.caramelo.neonteste.BaseTest
import okhttp3.mockwebserver.MockResponse
import org.junit.*
import org.mockito.Mockito.*


/**
 * Created by lucascaramelo on 15/03/2018.
 */
class HomeViewModelTest : BaseTest() {

    private lateinit var repository: NeonRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    override fun `before each test`() {
        super.`before each test`()

        repository = spy(componentTest.neonRepository)
        viewModel = spy(HomeViewModel(repository))
    }

    @Test
    fun `should get token and show my profile (Me)`() {
        val errorObserver: Observer<Throwable> = mock()
        val loadingObserver: Observer<Boolean> = mock()
        val meObserver: Observer<Me> = mock()

        val token = "meu token bonitão"

        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody("\"$token\""))

        viewModel.errorLiveData.observeForever(errorObserver)
        viewModel.loadingLiveData.observeForever(loadingObserver)
        viewModel.meLiveData?.observeForever(meObserver)

        verify(viewModel, times(1)).requestMe()
        verify(loadingObserver).onChanged(true)
        verify(repository).me()
        Thread.sleep(1000)

        Assert.assertEquals(token, TokenManager.token)

        verify(meObserver).onChanged(any())
        verify(errorObserver, never()).onChanged(any())
        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun `should throw a error when receive status code 401`() {
        val errorObserver: Observer<Throwable> = mock()
        val loadingObserver: Observer<Boolean> = mock()
        val meObserver: Observer<Me> = mock()

        server.enqueue(MockResponse()
                .setResponseCode(401))

        viewModel.errorLiveData.observeForever(errorObserver)
        viewModel.loadingLiveData.observeForever(loadingObserver)
        viewModel.meLiveData?.observeForever(meObserver)

        verify(viewModel, times(1)).requestMe()
        verify(loadingObserver).onChanged(true)
        verify(repository).me()
        Thread.sleep(1000)

        Assert.assertNull(TokenManager.token)

        verify(meObserver, never()).onChanged(any())
        verify(errorObserver, times(1)).onChanged(any())
        verify(loadingObserver).onChanged(false)
    }

}
