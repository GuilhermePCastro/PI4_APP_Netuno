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
import com.example.netuno.R
import com.example.netuno.databinding.CardPedidoBinding
import com.example.netuno.databinding.FragmentHistoricoCompraBinding
import com.example.netuno.databinding.FragmentListaPerfilBinding
import com.example.netuno.databinding.ProdutoCardBinding
import com.example.netuno.model.Pedido
import com.example.netuno.model.Produto
import com.example.netuno.ui.*
import com.example.netuno.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoricoCompraFragment : Fragment() {
    lateinit var  binding: FragmentHistoricoCompraBinding
    lateinit var ctx: Context
    lateinit var containerFrag: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoricoCompraBinding.inflate(inflater)

        if (container != null) {
            ctx = container.context
        }
        if (container != null) {
            containerFrag = container
        }

        verificaLogin()

        binding.swipperHistCom.setOnRefreshListener {
            atualizarPedidos()
        }

        atualizarPedidos()


        return binding.root
    }

    override fun onResume(){
        super.onResume()

        atualizarPedidos()
    }

    fun atualizarPedidos(){

        val callback = object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                CarregaOff()
                if(response.isSuccessful){

                    val pedidos = response.body()
                    atualizarUI(pedidos)

                }else{

                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        msg(binding.containerHistCompra,"Não é possível atualizar os pedidos")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                CarregaOff()
                msg(binding.containerHistCompra,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        var clienteId = retornaClienteId(ctx)

        if (clienteId != null) {
            API(ctx).cliente.pedidos(clienteId).enqueue(callback)
            CarregaOn()
        }

    }

    fun atualizarUI(lista: List<Pedido>?){
        binding.containerHistCompra.removeAllViews()

        lista?.forEach{
            val cardBinding = CardPedidoBinding.inflate(layoutInflater)

            cardBinding.lblData.text = formataData(it.created_at)
            cardBinding.lblTotPedido.text = "Total: R$ ${formataNumero(it.vl_total, "dinheiro")}"
            cardBinding.lblEnviado.text = "Status: ${it.ds_status}"
            cardBinding.lblPedido.text = "Pedido #${it.id}"

            var pedidoId = it.id

            binding.containerHistCompra.addView(cardBinding.root)

        }
    }

    fun CarregaOn(){
        binding.shimmerHistCompra.visibility = View.VISIBLE
        binding.shimmerHistCompra.startShimmer()
        binding.swipperHistCom.isRefreshing = true
    }

    fun CarregaOff() {
        binding.shimmerHistCompra.visibility = View.GONE
        binding.shimmerHistCompra.stopShimmer()
        binding.swipperHistCom.isRefreshing = false

    }

    fun chamaLogin() {
        containerFrag?.let {
            val i = Intent(containerFrag.context, LoginActivity::class.java)
            startActivity(i)
        }
    }

    fun verificaLogin(){
        // Se não tiver logado, vai para o login
        var token = retornaToken(ctx)
        if(token == ""){
            chamaLogin()
        }
    }
}