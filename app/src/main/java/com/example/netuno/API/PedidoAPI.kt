package com.example.netuno.API

import com.example.netuno.model.Carrinho
import com.example.netuno.model.Pedido
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PedidoAPI {

    @POST("/api/pedido/add")
    fun add(@Body pedido: Pedido): Call<List<Pedido>>

    @GET("/api/pedido")
    fun index(): Call<Pedido>

    @GET("/api/pedido/{id}")
    fun show(@Path("id") id: Int): Call<List<Pedido>>

}