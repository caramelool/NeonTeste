package br.com.caramelo.neonteste

import android.app.Application
import br.com.caramelo.neonteste.di.DaggerNeonComponent
import br.com.caramelo.neonteste.di.NeonComponent
import br.com.caramelo.neonteste.di.module.RepositoryModule
import br.com.caramelo.neonteste.di.module.RetrofitModule

/**
 * Created by lucascaramelo on 13/03/2018.
 */
class NeonApplication: Application()

val component: NeonComponent by lazy {
    DaggerNeonComponent.builder()
            .retrofitModule(RetrofitModule())
            .repositoryModule(RepositoryModule())
            .build()
}