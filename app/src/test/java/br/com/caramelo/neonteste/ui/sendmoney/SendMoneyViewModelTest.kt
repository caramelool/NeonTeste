package br.com.caramelo.neonteste.ui.sendmoney

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
class SendMoneyViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: NeonRepository
    @Mock
    lateinit var contact: Contact
    @Mock
    lateinit var repositoryLiveData: RepositoryLiveData<Boolean>

    private lateinit var viewModel: SendMoneyViewModel

    @Before
    fun `before each test`() {
        repositoryLiveData = mock()
        repository = mock {
            on { sendMoney(any(), any()) } doReturn repositoryLiveData
        }
        contact = mock()
        viewModel = spy(SendMoneyViewModel(repository, contact))
    }

    @Test
    fun `should bind the contact info when has a contact`() {
        val observer: Observer<Contact> = mock()
        viewModel.contactLiveData?.observeForever(observer)
        verify(viewModel).bindContact()
        verify(observer).onChanged(contact)
    }

    @Test
    fun `should display a error if the money was 0`() {
        val observer: Observer<SendMoneyProgress> = mock()

        val money = 0f

        viewModel.progressLiveData.observeForever(observer)
        viewModel.money = money
        viewModel.sendMoney()

        verify(observer).onChanged(SendMoneyProgress.Sending)
        verify(repository).sendMoney(contact, money)

        repositoryLiveData.postValue(false)

        verify(observer).onChanged(SendMoneyProgress.Error)
    }

    @Test
    fun `should display a success if the money was more then 0`() {
        val observer: Observer<SendMoneyProgress> = mock()

        val money = 100f

        viewModel.progressLiveData.observeForever(observer)
        viewModel.money = money
        viewModel.sendMoney()

        verify(observer).onChanged(SendMoneyProgress.Sending)
        verify(repository).sendMoney(contact, money)

        repositoryLiveData.postValue(true)

        verify(observer).onChanged(SendMoneyProgress.Success)
    }

}