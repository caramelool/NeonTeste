package br.com.caramelo.neonteste.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class BaseViewModel: ViewModel() {

    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Throwable>()
}