package br.com.caramelo.neonteste.data.repository

import br.com.caramelo.neonteste.data.model.Transfer
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*


/**
 * Created by lucascaramelo on 13/03/2018.
 */
interface NeonApi {
    @GET("GenerateToken")
    fun generateToken(@Query("nome") name: String,
                      @Query("email") email: String): Deferred<String>

    @FormUrlEncoded
    @POST("SendMoney")
    fun sendMoney(@Field("ClienteId") clientId: Int,
                  @Field("token") token: String?,
                  @Field("valor") value: Float): Deferred<Boolean>

    @GET("GetTransfers")
    @Headers("Content-Type: application/json")
    fun getTransfers(@Query("token") token: String?): Deferred<List<Transfer>>
}