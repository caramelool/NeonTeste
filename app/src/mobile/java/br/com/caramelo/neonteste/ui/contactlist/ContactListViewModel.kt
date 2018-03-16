package br.com.caramelo.neonteste.ui.contactlist

import android.arch.lifecycle.MutableLiveData
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.uibase.BaseViewModel

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class ContactListViewModel(
        private val repository: NeonRepository
): BaseViewModel() {

    var listLiveData: MutableLiveData<List<Contact>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                requestContactList()
            }
            return field
        }


    open fun requestContactList() {
        loadingLiveData.postValue(true)
        repository.listContact()
                .observer { contactList ->
                    listLiveData?.postValue(contactList)
                    loadingLiveData.postValue(false)
                }
                .observerThrowable {
                    errorLiveData.postValue(it)
                }
    }
}