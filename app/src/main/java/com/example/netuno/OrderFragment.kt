package com.example.netuno

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.API.API
import com.example.netuno.databinding.FragmentOrderBinding
import com.example.netuno.databinding.FragmentProdutoDescBinding
import com.example.netuno.fragments.HomeFragment
import com.example.netuno.model.Pedido
import com.example.netuno.model.Produto
import com.example.netuno.ui.alertFun
import com.example.netuno.ui.formataData
import com.example.netuno.ui.formataNumero
import com.example.netuno.ui.msg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PEDIDO_ID = "id"

class OrderFragment : Fragment() {

    lateinit var  binding: FragmentOrderBinding
    lateinit var containerPro: ViewGroup
    lateinit var ctx: Context

    //Parâmetros
    private var pedidoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            pedidoId = it.getInt(PEDIDO_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)

        if (container != null) {
            ctx = container.context
        }

        if (container != null) {
            containerPro = container
        }

        pegaPedido()

        return binding.root
    }

    fun pegaPedido(){

        val callback = object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                carregaOff()
                if(response.isSuccessful){
                    val pedido = response.body()
                    if (pedido != null) {
                        atualizarUI(pedido)
                    }

                }else{

                    msg(binding.scrollTela,"Não é possível carregar os dados do produto")
                    Log.e("ERROR", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                carregaOff()
                msg(binding.scrollTela,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }


        API(ctx).pedido.show(pedidoId!!).enqueue(callback)
        carregaOn()
    }

    fun atualizarUI(pedido: List<Pedido>){

        pedido.forEach {
            binding.txtCodPedido.text = "Pedido #${it.id}"
            binding.txtData.text = formataData(it.created_at)
            binding.txtStatusPed.text = "Status: ${it.ds_status}"
            binding.edtEnderecoPed.text = it.ds_endereco
            binding.edtNumeroPed.text = "Nº ${it.ds_numero}"
            binding.edtComplementoPed.text = "Complemento - ${it.ds_complemento}"
            binding.edtCEPped.text = "CEP ${it.ds_cep}"
            binding.edtCidadePed.text = it.ds_cidade
            binding.edtUFPed.text = it.ds_uf
            binding.txtValorFrete.text = "R$ ${formataNumero(it.vl_frete,"dinheiro")}"
            binding.txtValorProd.text = "R$ ${formataNumero(it.vl_total - it.vl_frete,"dinheiro")}"
            binding.txtValorTot.text = "R$ ${formataNumero(it.vl_total,"dinheiro")}"
        }

    }

    fun carregaOn(){
        binding.contDados.visibility = View.INVISIBLE
        binding.carregaPedido.visibility = View.VISIBLE
    }

    fun carregaOff(){
        binding.contDados.visibility = View.VISIBLE
        binding.carregaPedido.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putInt(PEDIDO_ID, id)
                }
            }
    }
}