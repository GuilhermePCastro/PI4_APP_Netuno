package com.example.netuno.API

import android.content.Context
import android.nfc.Tag
import com.example.netuno.model.Marca
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class API(val context: Context){

    private val baseUrl = "http://10.0.2.2:8000"
    private val timeout = 15L

    private val retrofit: Retrofit
        get(){

            //.authenticator(TokenAuthenticator(context))

            val okHttp = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttp)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    val produto: ProdutoAPI
        get(){
            return retrofit.create(ProdutoAPI::class.java)
        }

    val categoria: CategoriaAPI
        get(){
            return retrofit.create(CategoriaAPI::class.java)
        }

    val tag: TagAPI
        get(){
            return retrofit.create(TagAPI::class.java)
        }

    val marca: MarcaAPI
        get(){
            return retrofit.create(MarcaAPI::class.java)
        }
}