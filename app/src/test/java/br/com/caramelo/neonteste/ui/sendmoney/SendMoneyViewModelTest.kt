package br.com.caramelo.neonteste.ui.sendmoney

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
import org.mockito.Mock


/**
 * Created by lucascaramelo on 14/03/2018.
 */
@RunWith(JUnit4::class)
class SendMoneyViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var contact: Contact

    private lateinit var server: MockWebServer
    private lateinit var repository: NeonRepository
    private lateinit var viewModel: SendMoneyViewModel

    private val componentTest by lazy {
        getComponentForTest(server.url("/").toString())
    }

    @Before
    fun `before each test`() {
        server = MockWebServer()
        server.start()

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

        val money = 0f

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

    @After
    fun `after each test`() {
        server.shutdown()
    }

}