package br.com.caramelo.neonteste.ui.sendmoney

import android.arch.lifecycle.MutableLiveData
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.ui.base.BaseViewModel

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class SendMoneyViewModel(
        private val repository: NeonRepository,
        private val contact: Contact
): BaseViewModel() {

    var money: Float = 0f

    var progressLiveData = MutableLiveData<SendMoneyProgress>()
    var contactLiveData: MutableLiveData<Contact>? = null
            get() {
                if (field == null) {
                    field = MutableLiveData()
                    bindContact()
                }
                return field
            }

    open fun bindContact() {
        contactLiveData?.postValue(contact)
    }

    fun sendMoney() {
        progressLiveData.postValue(SendMoneyProgress.Sending)
        repository.sendMoney(contact, money)?.observer { success ->
            if (success == true) {
                progressLiveData.postValue(SendMoneyProgress.Success)
            } else {
                progressLiveData.postValue(SendMoneyProgress.Error)
            }
        }
    }
}