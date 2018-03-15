package br.com.caramelo.neonteste.ui.history

import android.arch.lifecycle.MutableLiveData
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.ui.base.BaseViewModel

/**
 * Created by lucascaramelo on 13/03/2018.
 */
class HistoryViewModel(
        private val repository: NeonRepository
) : BaseViewModel() {
    var listLiveData: MutableLiveData<List<Contact>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                requestContactList()
            }
            return field
        }


    private fun requestContactList() {
        loadingLiveData.postValue(true)
        repository.listContact(true)
                .observer { contactList ->
                    listLiveData?.postValue(contactList)
                    loadingLiveData.postValue(false)
                }
                .observerThrowable {
                    errorLiveData.postValue(it)
                }
    }

}