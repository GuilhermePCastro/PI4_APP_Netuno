package com.example.netuno.API

import com.example.netuno.model.*
import retrofit2.Call
import retrofit2.http.*

interface ClienteAPI {

    @GET("/api/cliente/index")
    fun index(): Call<List<Cliente>>

    @GET("/api/cliente/{id}")
    fun show(@Path("id") id: Int): Call<List<Cliente>>

    @GET("/api/cliente/{id}/enderecos")
    fun enderecos(@Path("id") id: Int): Call<Endereco>

    @GET("/api/cliente/{id}/pedidos")
    fun pedidos(@Path("id") id: Int): Call<List<Pedido>>

    @Multipart
    @POST("/api/cliente")
    fun store(@Body cliente: Cliente): Call<Cliente>

    @PUT("/api/cliente/{id}")
    fun update(@Body cliente: Cliente, @Path("id") id:Int): Call<Cliente>

    @DELETE("/api/cliente/{id}")
    fun delete(@Path("id") id: Int): Call<Cliente>
}