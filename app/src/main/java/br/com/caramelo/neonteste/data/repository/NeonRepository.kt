package br.com.caramelo.neonteste.data.repository

import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.model.Me
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.async

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class NeonRepository(private val api: NeonApi) {

    private var contactList: List<Contact>? = null
        get() {
            if (field == null) {
                val json = "[{\"id\":1,\"name\":\"Maria\",\"phone\":\"(11) 99423-2312\",\"email\":\"maria@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/women/1.jpg\"},{\"id\":2,\"name\":\"Carlos\",\"phone\":\"(11) 99053-0012\",\"email\":\"carlos@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/men/2.jpg\"},{\"id\":3,\"name\":\"Pedro\",\"phone\":\"(11) 99252-0521\",\"email\":\"pedro@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/men/3.jpg\"},{\"id\":4,\"name\":\"Henrique\",\"phone\":\"(11) 2234-5231\",\"email\":\"henrique@gmail.com\",\"image_url\":\"\"},{\"id\":5,\"name\":\"Isabelle\",\"phone\":\"(11) 99302-0502\",\"email\":\"isabelle@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/women/5.jpg\"},{\"id\":6,\"name\":\"Isabela\",\"phone\":\"(11) 99762-0502\",\"email\":\"isabela@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/women/6.jpg\"},{\"id\":7,\"name\":\"João Carlos\",\"phone\":\"(11) 99762-0502\",\"email\":\"joao.carlos@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/men/7.jpg\"},{\"id\":8,\"name\":\"Fatima\",\"phone\":\"(11) 2372-1122\",\"email\":\"fafa123@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/women/8.jpg\"},{\"id\":9,\"name\":\"Gabriela\",\"phone\":\"(11) 4423-4022\",\"email\":\"gabi@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/women/9.jpg\"},{\"id\":10,\"name\":\"Fernanda\",\"phone\":\"(11) 4423-4022\",\"email\":\"fe@gmail.com\",\"image_url\":\"\"},{\"id\":11,\"name\":\"Claudio\",\"phone\":\"(11) 9332-9292\",\"email\":\"claudio@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/men/11.jpg\"},{\"id\":12,\"name\":\"Michele\",\"phone\":\"(11) 9332-9292\",\"email\":\"michele@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/women/12.jpg\"},{\"id\":13,\"name\":\"Luiz Augusto\",\"phone\":\"(11) 9111-1119\",\"email\":\"lugusto@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/men/13.jpg\"},{\"id\":14,\"name\":\"Bruno Oliveira\",\"phone\":\"(11) 2311-1123\",\"email\":\"bruno@gmail.com\",\"image_url\":\"\"},{\"id\":15,\"name\":\"Victor\",\"phone\":\"(11) 9494-9002\",\"email\":\"victor@gmail.com\",\"image_url\":\"https://randomuser.me/api/portraits/men/15.jpg\"}]";
                field = Gson().fromJson(json, object : TypeToken<List<Contact>>() {}.type)
            }
            field?.forEach {
                it.transfer = 0f
            }
            return field
        }

    private val token: String?
        get() = TokenManager.token

    open fun me(): RepositoryLiveData<Me> {
        val liveData = RepositoryLiveData<Me>()
        async {
            val me = Me()
            try {
                TokenManager.token = api.generateToken(me.name, me.email).await()
            } catch (t: Throwable) {
                liveData.postThowable(t)
            }
            liveData.postValue(me)
        }
        return liveData
    }

    open fun listContact(withMoney: Boolean = false): RepositoryLiveData<List<Contact>> {
        val liveData = RepositoryLiveData<List<Contact>>()
        async {
            Thread.sleep(2000)
            val list = contactList
            if (withMoney) {
                try {
                    val transferList = api.getTransfers(token).await()
                    transferList.forEach { transfer ->
                        list?.find { it.id == transfer.clientId }?.let {
                            it.transfer += transfer.value
                        }
                    }
                } catch (t: Throwable) {
                    liveData.postThowable(t)
                }
            }
            val listOrdered = list?.sortedWith(
                    compareBy(Contact::name, Contact::name))
            liveData.postValue(listOrdered)
        }
        return liveData
    }

    open fun sendMoney(contact: Contact, money: Float): RepositoryLiveData<Boolean>? {
        val liveData = RepositoryLiveData<Boolean>()
        async {
            Thread.sleep(2000)
            try {
                val sent = api.sendMoney(contact.id, token, money).await()
                liveData.postValue(sent)
            } catch (t: Throwable) {
                liveData.postThowable(t)
                liveData.postValue(false)
            }
        }
        return liveData
    }

}