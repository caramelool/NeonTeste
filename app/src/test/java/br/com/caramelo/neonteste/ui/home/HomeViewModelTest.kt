package br.com.caramelo.neonteste.ui.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import br.com.caramelo.neonteste.data.model.Me
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.data.repository.TokenManager
import br.com.caramelo.neonteste.ui.getComponentForTest
import com.nhaarman.mockito_kotlin.*
import okhttp3.mockwebserver.MockResponse
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import okhttp3.mockwebserver.MockWebServer
import org.junit.*


/**
 * Created by lucascaramelo on 15/03/2018.
 */
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var server: MockWebServer
    private lateinit var repository: NeonRepository
    private lateinit var viewModel: HomeViewModel

    private val componentTest by lazy {
        getComponentForTest(server.url("/").toString())
    }

    @Before
    fun `before each test`() {
        server = MockWebServer()
        server.start()

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

    @After
    fun `after each test`() {
        server.shutdown()
    }

}
