package nl.vslcatena.lurvel2app.connections

import nl.vslcatena.lurvel2app.models.User
import nl.vslcatena.lurvel2app.utils.Env
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


object LurvelConnection {

    fun getUser(username: String, password: String): User? {
        val retrofit = Retrofit.Builder()
            .baseUrl(Env.LURVEL_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LurvelService::class.java)

        return service.getUser(username, password).execute().body()
    }
}

interface LurvelService {
    @FormUrlEncoded
    @POST("login")
    fun getUser(@Field("username") first: String, @Field("password") last: String): Call<User>
}