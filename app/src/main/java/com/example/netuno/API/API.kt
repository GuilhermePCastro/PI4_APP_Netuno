package com.example.netuno.API

import android.content.Context
import android.nfc.Tag
import com.example.netuno.model.Marca
import com.example.netuno.ui.retornaToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

import com.google.gson.Gson
import okhttp3.Request


class API(val context: Context){

    private val baseUrl = "http://10.0.2.2:8000"
    private val timeout = 15L

    private val retrofit: Retrofit
        get(){

            var token = retornaToken(context)

            val okHttp = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${token}"
                        )
                        .build()
                    chain.proceed(newRequest)
                }
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

    val user: UserAPI
        get(){
            return retrofit.create(UserAPI::class.java)
        }

    val pedido: PedidoAPI
        get(){
            return retrofit.create(PedidoAPI::class.java)
        }

    val cliente: ClienteAPI
        get(){
            return retrofit.create(ClienteAPI::class.java)
        }

    val carrinho: CarrinhoAPI
        get(){
            return retrofit.create(CarrinhoAPI::class.java)
        }

    val endereco: EnderecoAPI
        get(){
            return retrofit.create(EnderecoAPI::class.java)
        }
}