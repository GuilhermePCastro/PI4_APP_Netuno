package com.example.netuno.API

import retrofit2.Call
import retrofit2.http.*
import com.example.netuno.model.Produto

interface ProdutoAPI {

    @GET("/api/produto")
    fun index(): Call<List<Produto>>

    @GET("/api/produto/destaques")
    fun destaques(): Call<List<Produto>>

    @GET("/api/produto/lancamentos")
    fun lancamentos(): Call<List<Produto>>

    @GET("/api/produto/{id}")
    fun show(@Path("id") id: Int): Call<List<Produto>>

    @GET("/api/produto/filtro")
    fun filtro(@Query("nome") nome: String): Call<List<Produto>>

}