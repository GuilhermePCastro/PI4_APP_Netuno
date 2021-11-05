package com.example.netuno.API

import com.example.netuno.model.Cliente
import com.example.netuno.model.Endereco
import com.example.netuno.model.Pedido
import com.example.netuno.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {

    @GET("/api/user/index")
    fun index(): Call<List<User>>

    @Headers("Content-Type: application/json")
    @GET("/api/user")
    fun show(): Call<User>

    @GET("/api/user/{id}")
    fun showUnique(@Path("id") id: Int): Call<User>

    @Headers("Content-Type: application/json")
    @POST("/api/login")
    fun login(@Body user: User): Call<User>

    @POST("/api/user")
    fun store(@Body user: User): Call<User>

    @PUT("/api/user/{id}")
    fun update(@Body user: User, @Path("id") id:Int): Call<User>

    @DELETE("/api/user/{id}")
    fun delete(@Path("id") id: Int): Call<User>

}