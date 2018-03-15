package br.com.caramelo.neonteste.data.repository

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class RepositoryLiveData<T> {

    private var observerValue: (T?) -> Unit = {}
    private var observerThrowable: (t: Throwable?) -> Unit = {}

    fun observer(observer: (response: T?) -> Unit): RepositoryLiveData<T> {
        observerValue = observer
        return this
    }

    fun observerThrowable(observer: (t: Throwable?) -> Unit): RepositoryLiveData<T> {
        observerThrowable = observer
        return this
    }

    fun postValue(value: T?) {
        observerValue(value)
    }

    fun postThowable(t: Throwable?) {
        observerThrowable(t)
    }
}