package com.example.netuno

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.API.API
import com.example.netuno.databinding.FragmentCategoriasBinding
import com.example.netuno.databinding.FragmentCheckoutBinding
import com.example.netuno.databinding.ProdutoCarrinhoBinding
import com.example.netuno.databinding.ProdutoCheckoutBinding
import com.example.netuno.fragments.CarrinhoFragment
import com.example.netuno.fragments.HistoricoCompraFragment
import com.example.netuno.fragments.ProdutoDescFragment
import com.example.netuno.model.*
import com.example.netuno.ui.*
import com.example.netuno.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutFragment : Fragment() {

    lateinit var  binding: FragmentCheckoutBinding
    lateinit var containerFrag: ViewGroup
    lateinit var ctx: Context

    //Dados para gerar pedido
    var total = 0.00
    var frete = 0.00

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCheckoutBinding.inflate(inflater)
        if (container != null) {
            ctx = container.context
        }

        if (container != null) {
            containerFrag = container
        }

        binding.edtCEP.addTextChangedListener(Mask.mask("#####-###", binding.edtCEP))

        verificaLogin()
        atualizaDados()

        binding.extendedFab.setOnClickListener {
            if(validaDados()){
                geraPedido(total, frete)
            }

        }

        return binding.root
    }

    fun chamaLogin() {
        containerFrag?.let{
            val i = Intent(containerFrag.context, LoginActivity::class.java)
            startActivity(i)

        }
    }

    fun verificaLogin() {
        // Se não tiver logado, vai para o login
        var token = retornaToken(ctx)
        if (token == "") {
            chamaLogin()
        }
    }

    fun atualizaDados(){
        carregaOn()
        atualizaValores()
        atualizaEnd()

    }

    fun atualizaEnd(){
        val callback = object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                if (response.isSuccessful) {

                    val endereco = response.body()
                    if (endereco != null) {
                        binding.edtCEP.setText(endereco.ds_cep)
                        binding.edtEndereco.setText(endereco.ds_endereco)
                        binding.edtNumero.setText(endereco.ds_numero)
                        binding.edtComplemento.setText(endereco.ds_complemento)
                        binding.edtCidade.setText(endereco.ds_cidade)
                        binding.edtUF.setText(endereco.ds_uf)
                    }

                } else {

                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.cardView2, "Não é possível carregar os dados do endereço")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                msg(binding.cardView2, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }

        var clienteId = retornaClienteId(ctx)
        API(ctx).cliente.enderecos(clienteId).enqueue(callback)
    }

    fun atualizaValores(){

        val callback = object : Callback<Carrinho> {
            override fun onResponse(call: Call<Carrinho>, response: Response<Carrinho>) {

                if(response.isSuccessful){

                    val carrinho = response.body()

                    if (carrinho != null) {

                        //Se não tiver produto, vai para o carrinho
                        if(carrinho.valor == 0.0){
                            containerFrag?.let{
                                parentFragmentManager.beginTransaction().replace(it.id, CarrinhoFragment()).addToBackStack("Carrinho").commit()
                            }
                        }

                        frete = 00.00
                        total = carrinho.valor

                        binding.lblFrete.text = "R$ ${formataNumero(frete,"dinheiro")}"
                        binding.lblTotProd.text =  "R$ ${formataNumero(carrinho.valor, "dinheiro")}"
                        binding.lblVlTotal.text = "R$ ${formataNumero(frete + carrinho.valor, "dinheiro")}"
                        atualizarUI(carrinho.itens)

                    }

                }else{
                    carregaOff()
                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        msg(binding.cardView2,"Não é possível atualizar o carrinho")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Carrinho>, t: Throwable) {
                msg(binding.cardView2,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        var clienteId = retornaClienteId(ctx)

        if (clienteId != null) {
            API(ctx).carrinho.show(clienteId).enqueue(callback)
        }
    }

    fun atualizarUI(carrinho: List<CarrinhoItem>?){
        carregaOn()
        binding.contItensPed.removeAllViews()

        carrinho?.forEach{


            val cardBinding = ProdutoCheckoutBinding.inflate(layoutInflater)
            var idProduto = it.produto_id

            cardBinding.lblQuantCheck.text = it.qt_produto.toString()

            cardBinding.imgRemove.setOnClickListener {
                deleteProduto(idProduto)
            }

            cardBinding.cardView.setOnClickListener {
                containerFrag?.let {
                    parentFragmentManager.beginTransaction().replace(it.id,  ProdutoDescFragment.newInstance(idProduto))
                        .addToBackStack("Home").commit()
                }
            }

            val callback = object : Callback<List<Produto>> {
                override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {

                    if(response.isSuccessful){
                        val produto = response.body()

                        if (produto != null) {


                            produto.forEach {
                                var precoProduto = it.vl_produto * cardBinding.lblQuantCheck.text.toString().toInt()

                                cardBinding.lblNomePro.text         = it.ds_nome
                                cardBinding.lblPrecoUni.text        =  " - R$ ${formataNumero(it.vl_produto, "dinheiro")}"
                                cardBinding.lblValorTotProduto.text = "R$ ${formataNumero(precoProduto, "dinheiro")}"

                                //Montando o shimmer para o picaso usar
                                var sDrawable = montaShimmerPicaso()

                                if(it.ds_linkfoto.isNotEmpty()){
                                    Picasso.get()
                                        .load(it.ds_linkfoto)
                                        .placeholder(sDrawable)
                                        .error(R.drawable.no_image)
                                        .into(cardBinding.imgProduto)
                                }

                                if(carrinho.size > binding.contItensPed.childCount){
                                    binding.contItensPed.addView(cardBinding.root)

                                }

                                if(carrinho.size == binding.contItensPed.childCount){
                                    carregaOff()

                                }
                            }




                        }

                    }else{
                        carregaOff()
                        msg(binding.cardView2,"Não é possível atualizar o produto")
                        Log.e("ERROR", response.errorBody().toString())
                    }


                }

                override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                    carregaOff()
                    msg(binding.cardView2,"Não é possível se conectar ao servidor")
                    Log.e("ERROR", "Falha ao executar serviço", t)

                }



            }
            API(ctx).produto.show(idProduto).enqueue(callback)




        }


    }

    fun deleteProduto(id: Int){

        val callback = object : Callback<CarrinhoItem> {
            override fun onResponse(call: Call<CarrinhoItem>, response: Response<CarrinhoItem>) {
                if(response.isSuccessful){

                    val carrinho = response.body()

                    if (carrinho != null) {
                        atualizaDados()
                    }

                }else{

                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        msg(binding.cardView2,"Não é possível atualizar o carrinho")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<CarrinhoItem>, t: Throwable) {
                msg(binding.cardView2,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).carrinho.delete(id).enqueue(callback)
    }

    fun geraPedido(totalPedido: Double, frete: Double){

        val callback = object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {

                //CarregaOff()
                if (response.isSuccessful) {

                    val pedido = response.body()
                    if (pedido != null) {

                        var alert = alertFun("Pedido #${pedido[0].id} Gerado!", "A Netuno agradece a compra :)", ctx)

                        if (alert != null) {
                            alert.setOnDismissListener {
                                containerFrag?.let {
                                    parentFragmentManager.beginTransaction().replace(it.id,  OrderFragment.newInstance(pedido[0].id))
                                        .addToBackStack("ListaCategoria").commit()
                                }
                            }
                            alert.create().show()
                        }

                    }

                } else {
                    carregaOff()
                    if (response.code() == 401) {
                        chamaLogin()
                    } else {

                    }

                    Log.e("ERROR", response.code().toString())
                    Log.e("ERROR", response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                carregaOff()
                msg(binding.cardView2, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }



        var pedido = Pedido(
            "Em aberto",
            totalPedido,
            binding.edtNumero.text.toString(),
            "",
            binding.edtCidade.text.toString(),
            "",
            binding.edtUF.text.toString(),
            0,
            binding.edtCEP.text.toString(),
            frete,
            binding.edtComplemento.text.toString(),
            retornaClienteId(ctx),
            binding.edtEndereco.text.toString(),
            null
        )
        API(ctx).pedido.add(pedido).enqueue(callback)
        carregaOn()

    }

    fun carregaOn(){
        binding.contDados.visibility = View.INVISIBLE
        binding.progressCheck.visibility = View.VISIBLE
    }

    fun carregaOff(){
        binding.contDados.visibility = View.VISIBLE
        binding.progressCheck.visibility = View.GONE
    }

    fun validaDados(): Boolean {

        var retorno = true

        if(binding.edtCEP.text.isEmpty()){
            msg(binding.contPrincipal, "Campo CEP vázio!")
            binding.edtCEP.requestFocus()

            retorno = false

        }else if(binding.edtCidade.text.isEmpty()){
            msg(binding.contPrincipal, "Campo Cidade vázio!")
            binding.edtCidade.requestFocus()

            retorno = false

        }else if(binding.edtEndereco.text.isEmpty()){
            msg(binding.contPrincipal, "Campo Endereço vázio!")
            binding.edtEndereco.requestFocus()

            retorno = false

        }else if(binding.edtNumero.text.isEmpty()){
            msg(binding.contPrincipal, "Campo Número vázio!")
            binding.edtNumero.requestFocus()

            retorno = false

        }else if(binding.edtUF.text.isEmpty()){
            msg(binding.contPrincipal, "Campo UF vázio!")
            binding.edtUF.requestFocus()

            retorno = false

        }else if(binding.edtComplemento.text.isEmpty()){
            binding.edtComplemento.setText("N/A")
        }else if(binding.edtCEP.text.length != 9) {

            msg(binding.edtComplemento, "Preencha o campo de CEP corretamente!")
            binding.edtCEP.requestFocus()

            retorno = false
        }

        return retorno

    }



}