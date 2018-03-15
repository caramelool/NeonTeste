package br.com.caramelo.neonteste.ui.contactlist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.ui.getComponentForTest
import com.nhaarman.mockito_kotlin.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by lucascaramelo on 14/03/2018.
 */
@RunWith(JUnit4::class)
class ContactListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var server: MockWebServer
    private lateinit var repository: NeonRepository
    private lateinit var viewModel: ContactListViewModel

    private val componentTest by lazy {
        getComponentForTest(server.url("/").toString())
    }

    @Before
    fun `before each test`() {
        server = MockWebServer()
        server.start()

        repository = spy(componentTest.neonRepository)
        viewModel = spy(ContactListViewModel(repository))
    }

    @Test
    fun `should receiver a contact list to display in UI`() {
        val loadingObserver: Observer<Boolean> = mock()
        val listObserver: Observer<List<Contact>> = mock()

        server.enqueue(MockResponse()
                .setResponseCode(200))

        viewModel.loadingLiveData.observeForever(loadingObserver)
        viewModel.listLiveData?.observeForever(listObserver)

        verify(viewModel).requestContactList()
        verify(loadingObserver).onChanged(true)
        verify(repository, times(1)).listContact(false)
        Thread.sleep(1000)

        verify(listObserver).onChanged(any())
        verify(loadingObserver).onChanged(false)

    }

    @After
    fun `after each test`() {
        server.shutdown()
    }

}