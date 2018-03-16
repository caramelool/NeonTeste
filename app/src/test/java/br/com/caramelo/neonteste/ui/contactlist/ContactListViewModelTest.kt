package br.com.caramelo.neonteste.ui.contactlist

import android.arch.lifecycle.Observer
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.BaseTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*

/**
 * Created by lucascaramelo on 14/03/2018.
 */
class ContactListViewModelTest : BaseTest() {

    private lateinit var repository: NeonRepository
    private lateinit var viewModel: ContactListViewModel

    @Before
    override fun `before each test`() {
        super.`before each test`()
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
}