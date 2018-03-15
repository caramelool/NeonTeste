package br.com.caramelo.neonteste.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lucascaramelo on 13/03/2018.
 */
data class Transfer(
        @SerializedName("Id") val id: Int,
        @SerializedName("ClienteId") val clientId: Int,
        @SerializedName("Valor") val value: Float
)