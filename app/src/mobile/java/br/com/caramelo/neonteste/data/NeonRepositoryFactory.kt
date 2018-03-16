package br.com.caramelo.neonteste.data

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import br.com.caramelo.neonteste.component
import br.com.caramelo.neonteste.data.repository.NeonRepository
import javax.inject.Inject

/**
 * Created by lucascaramelo on 13/03/2018.
 */
class NeonRepositoryFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var neonRepository: NeonRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NeonRepository::class.java)
                .newInstance(neonRepository)
    }

    init {
        component.inject(this)
    }
}