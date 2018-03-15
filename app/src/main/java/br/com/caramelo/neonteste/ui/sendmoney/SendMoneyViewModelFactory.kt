package br.com.caramelo.neonteste.ui.sendmoney

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import br.com.caramelo.neonteste.component
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import javax.inject.Inject

/**
 * Created by lucascaramelo on 13/03/2018.
 */
class SendMoneyViewModelFactory(val contact: Contact): ViewModelProvider.Factory {

    @Inject
    lateinit var neonRepository: NeonRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SendMoneyViewModel(neonRepository, contact) as T
    }

    init {
        component.inject(this)
    }
}