package com.example.netuno.API

import com.example.netuno.model.Categoria
import com.example.netuno.model.Produto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriaAPI {

    @GET("/api/categoria")
    fun index(): Call<List<Categoria>>

    @GET("/api/categoria/{id}")
    fun show(@Path("id") id: Int): Call<Categoria>

    @GET("/api/categoria/{id}/produtos")
    fun produtos(): Call<List<Produto>>
}