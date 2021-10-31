package com.example.netuno.API

import com.example.netuno.model.Categoria
import com.example.netuno.model.Produto
import com.example.netuno.model.Tag
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TagAPI {

    @GET("/api/tag")
    fun index(): Call<List<Tag>>

    @GET("/api/tag/{id}")
    fun show(@Path("id") id: Int): Call<Tag>

    @GET("/api/tag/{id}/produtos")
    fun produtos(@Path("id") id: Int): Call<List<Produto>>

    @GET("/api/tag/filtro")
    fun filtro(): Call<List<Tag>>

}