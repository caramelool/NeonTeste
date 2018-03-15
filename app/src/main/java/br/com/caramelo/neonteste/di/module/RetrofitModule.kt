package br.com.caramelo.neonteste.di.module

import br.com.caramelo.neonteste.BuildConfig
import br.com.caramelo.neonteste.data.repository.TokenManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by lucascaramelo on 13/03/2018.
 */
@Module
class RetrofitModule {

    @Singleton
    @Provides
    @Named("baseUrl")
    fun baseUrl(): String {
        return "https://processoseletivoneon.neonhomol.com.br/"
    }

    @Singleton
    @Provides
    fun httpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)

        clientBuilder.addInterceptor {
            val original = it.request()

            val requestBuilder = original.newBuilder()

            /** o token poderia ser trafegado pelo header, mais seguro e mais bonito (: **/
            TokenManager.token?.let {
                requestBuilder.addHeader("Authorization", "JWT $it")
            }

            val request = requestBuilder.build()
            it.proceed(request)
        }

        val level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
        val logging = HttpLoggingInterceptor()
        logging.level = level
        clientBuilder.addInterceptor(logging)

        return clientBuilder.build()
    }

    @Singleton
    @Provides
    fun retrofit(@Named("baseUrl") urlBase: String,
                 httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(httpClient)
                .build()
    }
}