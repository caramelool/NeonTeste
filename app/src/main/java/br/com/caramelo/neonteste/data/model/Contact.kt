package br.com.caramelo.neonteste.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by lucascaramelo on 13/03/2018.
 */
open class Contact(
        val id: Int,
        val name: String,
        val phone: String,
        @SerializedName("image_url") val image: String? = null,
        var transfer: Float = 0f
): Serializable