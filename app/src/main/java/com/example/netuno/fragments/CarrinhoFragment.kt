package com.example.netuno.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.API.API
import com.example.netuno.databinding.CardPedidoBinding
import com.example.netuno.databinding.FragmentCarrinhoBinding
import com.example.netuno.model.Carrinho
import com.example.netuno.ui.formataData
import com.example.netuno.ui.formataNumero
import com.example.netuno.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarrinhoFragment : Fragment() {
    lateinit var  binding: FragmentCarrinhoBinding
    lateinit var ctx: Context
    lateinit var containerFrag: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCarrinhoBinding.inflate(inflater)

        if (container != null) {
            ctx = container.context
        }
        if (container != null) {
            containerFrag = container
        }

        return binding.root
    }

    fun atualizaCarrinho(){

        val callback = object : Callback<Carrinho> {
            override fun onResponse(call: Call<Carrinho>, response: Response<Carrinho>) {

                if(response.isSuccessful){

                    val carrinho = response.body()
                    atualizarUI(carrinho)

                }else{

                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        Snackbar.make(binding.containerCarrinho,"Não é possível atualizar o carrinho",
                            Snackbar.LENGTH_LONG).show()
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Carrinho>, t: Throwable) {

                Snackbar.make(binding.containerCarrinho,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).carrinho.show().enqueue(callback)



    }

    fun atualizarUI(carrinho: Carrinho?){
        binding.containerCarrinho.removeAllViews()


            val cardBinding = CardPedidoBinding.inflate(layoutInflater)

            binding.containerCarrinho.addView(cardBinding.root)


    }

    fun chamaLogin() {
        containerFrag?.let {
            val i = Intent(containerFrag.context, LoginActivity::class.java)
            startActivity(i)
        }
    }


}