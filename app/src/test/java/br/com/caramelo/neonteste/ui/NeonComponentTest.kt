package br.com.caramelo.neonteste.ui

import br.com.caramelo.neonteste.di.DaggerNeonComponent
import br.com.caramelo.neonteste.di.NeonComponent
import br.com.caramelo.neonteste.di.module.RepositoryModule
import br.com.caramelo.neonteste.di.module.RetrofitModule

/**
 * Created by lucascaramelo on 15/03/2018.
 */

fun getComponentForTest(baseUrl: String): NeonComponent {
    return DaggerNeonComponent.builder()
            .retrofitModule(RetrofitModule(baseUrl))
            .repositoryModule(RepositoryModule())
            .build()
}