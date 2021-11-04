package com.example.netuno.API

import com.example.netuno.model.Carrinho
import com.example.netuno.model.Categoria
import com.example.netuno.model.Produto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CarrinhoAPI {

    @GET("/api/carrinho/add/{id}")
    fun add(@Path("id") id: Int): Call<Carrinho>

    @GET("/api/carrinho/remove/{id}")
    fun remove(@Path("id") id: Int): Call<Carrinho>

    @GET("/api/carrinho")
    fun show(): Call<Carrinho>
}