package br.com.caramelo.neonteste.di.module

import br.com.caramelo.neonteste.data.repository.NeonApi
import br.com.caramelo.neonteste.data.repository.NeonRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by lucascaramelo on 13/03/2018.
 */
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun neonRepository(retrofit: Retrofit): NeonRepository {
        val api = retrofit.create(NeonApi::class.java)
        return NeonRepository(api)
    }
}