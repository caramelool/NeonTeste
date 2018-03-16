package br.com.caramelo.neonteste.di

import br.com.caramelo.neonteste.data.NeonRepositoryFactory
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.di.module.RepositoryModule
import br.com.caramelo.neonteste.di.module.RetrofitModule
import br.com.caramelo.neonteste.ui.sendmoney.SendMoneyViewModelFactory
import dagger.Component
import javax.inject.Singleton

/**
 * Created by lucascaramelo on 13/03/2018.
 */
@Singleton
@Component(modules = [
    RetrofitModule::class,
    RepositoryModule::class])
interface NeonComponent {

    val neonRepository: NeonRepository

    fun inject(factory: NeonRepositoryFactory)
    fun inject(factory: SendMoneyViewModelFactory)
}