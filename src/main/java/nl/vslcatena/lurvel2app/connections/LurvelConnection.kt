package nl.vslcatena.lurvel2app.connections

import nl.vslcatena.lurvel2app.models.Committee
import nl.vslcatena.lurvel2app.models.User
import nl.vslcatena.lurvel2app.utils.Env
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


object LurvelConnection {
    val retroService = Retrofit.Builder()
        .baseUrl(Env.LURVEL_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LurvelService::class.java)

    fun getUser(username: String, password: String): User? {
        return retroService.getUser(username, password).execute().body()
    }

    fun getAllCommittees(): List<Committee>? {
        return retroService.getAllCommittees().execute().body()
    }

    fun getUserCommittees(userId: String): Array<String>? {
        return retroService.getUserCommittees(userId).execute().body()
    }
}

interface LurvelService {
    @FormUrlEncoded
    @POST("login")
    fun getUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<User>


    @GET("committees")
    fun getAllCommittees(): Call<List<Committee>>


    @GET("committees")
    fun getUserCommittees(
        @Field("userId") userId: String
    ): Call<Array<String>>
}