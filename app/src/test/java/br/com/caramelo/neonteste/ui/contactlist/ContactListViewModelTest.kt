package br.com.caramelo.neonteste.ui.contactlist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.data.repository.RepositoryLiveData
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock


/**
 * Created by lucascaramelo on 14/03/2018.
 */
@RunWith(JUnit4::class)
class ContactListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: NeonRepository
    @Mock
    lateinit var repositoryLiveData: RepositoryLiveData<List<Contact>>

    private lateinit var viewModel: ContactListViewModel

    @Before
    fun `before each test`() {
        repositoryLiveData = mock()
        repository = mock {
            on { listContact(any()) } doReturn repositoryLiveData
        }
        viewModel = spy(ContactListViewModel(repository))
    }

    @Test
    fun `should receiver a contact list to display in UI`() {
        val loadingObserver: Observer<Boolean> = mock()
        val listObserver: Observer<List<Contact>> = mock()
        val list: List<Contact> = mock()

        viewModel.loadingLiveData.observeForever(loadingObserver)
        viewModel.listLiveData?.observeForever(listObserver)

        verify(viewModel).requestContactList()
        verify(loadingObserver).onChanged(true)
        verify(repository).listContact(false)

        repositoryLiveData.postValue(list)

        verify(listObserver).onChanged(list)
        verify(loadingObserver).onChanged(false)

    }

}