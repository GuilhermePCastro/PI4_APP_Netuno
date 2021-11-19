package com.example.netuno.API

import com.example.netuno.model.CEP
import com.example.netuno.model.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CEPAPI {

    @GET("/ws/{cep}/json/")
    fun CEP(@Path("cep") cep: String): Call<CEP>

}