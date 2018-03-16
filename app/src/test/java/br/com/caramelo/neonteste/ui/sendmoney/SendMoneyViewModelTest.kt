package br.com.caramelo.neonteste.ui.sendmoney

import android.arch.lifecycle.Observer
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.BaseTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*


/**
 * Created by lucascaramelo on 14/03/2018.
 */
class SendMoneyViewModelTest: BaseTest() {

    @Mock
    lateinit var contact: Contact

    private lateinit var repository: NeonRepository
    private lateinit var viewModel: SendMoneyViewModel

    @Before
    override fun `before each test`() {
        super.`before each test`()
        contact = mock()
        repository = spy(componentTest.neonRepository)
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

        viewModel.progressLiveData.observeForever(observer)

        val money = 0f

        viewModel.money = money
        viewModel.sendMoney()

        verify(observer).onChanged(SendMoneyProgress.Error)
        verify(repository, never()).sendMoney(contact, money)
        verify(observer, never()).onChanged(SendMoneyProgress.Success)
    }

    @Test
    fun `should display a error if server return a error`() {
        val observer: Observer<SendMoneyProgress> = mock()

        viewModel.progressLiveData.observeForever(observer)

        val money = 100f

        server.enqueue(MockResponse()
                .setResponseCode(400))

        viewModel.progressLiveData.observeForever(observer)
        viewModel.money = money
        viewModel.sendMoney()

        verify(observer).onChanged(SendMoneyProgress.Sending)
        verify(repository, times(1)).sendMoney(contact, money)
        Thread.sleep(1000)

        verify(observer, never()).onChanged(SendMoneyProgress.Success)
        verify(observer).onChanged(SendMoneyProgress.Error)
    }

    @Test
    fun `should display a success if the money was more then 0`() {
        val observer: Observer<SendMoneyProgress> = mock()

        val money = 100f

        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody("\"true\""))

        viewModel.progressLiveData.observeForever(observer)
        viewModel.money = money
        viewModel.sendMoney()

        verify(observer).onChanged(SendMoneyProgress.Sending)
        verify(repository, times(1)).sendMoney(contact, money)
        Thread.sleep(1000)

        verify(observer).onChanged(SendMoneyProgress.Success)
        verify(observer, never()).onChanged(SendMoneyProgress.Error)
    }

}