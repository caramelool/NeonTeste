package br.com.caramelo.neonteste

import android.arch.core.executor.testing.InstantTaskExecutorRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

/**
 * Created by lucascaramelo on 16/03/2018.
 */
@RunWith(JUnit4::class)
open class BaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    val server: MockWebServer by lazy {
        MockWebServer()
    }

    val componentTest by lazy {
        getComponentForTest(server.url("/").toString())
    }

    @Before
    open fun `before each test`() {
        server.start()
    }

    @After
    open fun `after each test`() {
        server.shutdown()
    }

    inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)
}