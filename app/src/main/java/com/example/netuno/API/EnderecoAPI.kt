package com.example.netuno.API

import com.example.netuno.model.Cliente
import com.example.netuno.model.Endereco
import com.example.netuno.model.Pedido
import retrofit2.Call
import retrofit2.http.*

interface EnderecoAPI {

    @GET("/api/endereco/index")
    fun index(): Call<List<Endereco>>

    @GET("/api/endereco/{id}")
    fun show(@Path("id") id: Int): Call<Endereco>

    @POST("/api/endereco")
    fun store(@Body endereco: Endereco): Call<Endereco>

    @PUT("/api/endereco/{id}")
    fun update(@Body endereco: Endereco, @Path("id") id:Int): Call<Endereco>

    @DELETE("/api/endereco/{id}")
    fun delete(@Path("id") id: Int): Call<Endereco>

}