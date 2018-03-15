package br.com.caramelo.neonteste.ui.home

import android.arch.lifecycle.MutableLiveData
import br.com.caramelo.neonteste.data.model.Me
import br.com.caramelo.neonteste.data.repository.NeonRepository
import br.com.caramelo.neonteste.ui.base.BaseViewModel

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class HomeViewModel(
        private val neonRepository: NeonRepository
): BaseViewModel() {

    var meLiveData: MutableLiveData<Me>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                requestMe()
            }
            return field
        }

    open fun requestMe() {
        loadingLiveData.postValue(true)
        neonRepository.me()
                .observer { me ->
                    meLiveData?.postValue(me)
                    loadingLiveData.postValue(false)
                }
                .observerThrowable {
                    errorLiveData.postValue(it)
                    loadingLiveData.postValue(false)
                }
    }
}