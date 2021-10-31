package com.example.netuno.API

import com.example.netuno.model.Marca
import com.example.netuno.model.Produto
import com.example.netuno.model.Tag
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MarcaAPI {

    @GET("/api/marca")
    fun index(): Call<List<Marca>>

    @GET("/api/marca/{id}")
    fun show(@Path("id") id: Int): Call<Marca>

    @GET("/api/marca/{id}/produtos")
    fun produtos(@Path("id") id: Int): Call<List<Produto>>

    @GET("/api/marca/filtro")
    fun filtro(): Call<List<Marca>>

}