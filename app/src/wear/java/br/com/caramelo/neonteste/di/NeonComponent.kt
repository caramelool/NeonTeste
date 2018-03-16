package br.com.caramelo.neonteste.di

import br.com.caramelo.neonteste.di.module.RepositoryModule
import br.com.caramelo.neonteste.di.module.RetrofitModule
import br.com.caramelo.neonteste.ui.MainActivity
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
    fun inject(activity: MainActivity)
}