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
import com.example.netuno.databinding.FragmentCarrinhoBinding
import com.example.netuno.databinding.ProdutoCarrinhoBinding
import com.example.netuno.model.Carrinho
import com.example.netuno.model.CarrinhoItem
import com.example.netuno.model.Produto
import com.example.netuno.ui.*
import com.example.netuno.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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

        verificaLogin()

        binding.swipperCarrinho.setOnRefreshListener {
            atualizaCarrinho()
        }

        atualizaCarrinho()


        return binding.root
    }

    override fun onResume(){
        super.onResume()

        atualizaCarrinho()
    }

    fun atualizaCarrinho(){

        val callback = object : Callback<Carrinho> {
            override fun onResponse(call: Call<Carrinho>, response: Response<Carrinho>) {
                CarregaOff()
                if(response.isSuccessful){

                    val carrinho = response.body()

                    if (carrinho != null) {
                        binding.lbTotal.text =  "R$ ${formataNumero(carrinho.valor, "dinheiro")}"
                        atualizarUI(carrinho.itens)

                    }

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
                CarregaOff()
                Snackbar.make(binding.containerCarrinho,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        var clienteId = retornaClienteId(ctx)

        if (clienteId != null) {
            API(ctx).carrinho.show(clienteId).enqueue(callback)
            CarregaOn()
        }



    }

    fun atualizarUI(carrinho: List<CarrinhoItem>?){
        binding.containerCarrinho.removeAllViews()

        if (carrinho != null) {
            if(carrinho.isEmpty()){
                binding.lblSemProd.visibility = View.VISIBLE
            }
        }

        carrinho?.forEach{
            val cardBinding = ProdutoCarrinhoBinding.inflate(layoutInflater)
            var idProduto = it.produto_id

            cardBinding.lblQuantidade.text = it.qt_produto.toString()

            pegaProduto(idProduto, cardBinding)

            cardBinding.btnAdd.setOnClickListener {
                addProduto(idProduto)
            }

            cardBinding.btnRemove.setOnClickListener {
                removeProduto(idProduto)
            }

            cardBinding.imgRemove.setOnClickListener {
                deleteProduto(idProduto)
            }

            cardBinding.cardView.setOnClickListener {
                containerFrag?.let {
                    parentFragmentManager.beginTransaction().replace(it.id,  ProdutoDescFragment.newInstance(idProduto))
                        .addToBackStack("Home").commit()
                }
            }

            binding.containerCarrinho.addView(cardBinding.root)

        }




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

    fun pegaProduto(id: Int, cardBinding: ProdutoCarrinhoBinding){

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                CarregaOff()
                if(response.isSuccessful){
                    val produto = response.body()

                    if (produto != null) {
                        produto.forEach {
                            var precoProduto = it.vl_produto * cardBinding.lblQuantidade.text.toString().toInt()

                            cardBinding.lblNomePro.text = it.ds_nome
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


                        }
                    }

                }else{
                    Snackbar.make(binding.scrollView,"Não é possível atualizar o produto",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                CarregaOff()
                Snackbar.make(binding.scrollView,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).produto.show(id).enqueue(callback)
        CarregaOn()

    }

    fun addProduto(id: Int){

        val callback = object : Callback<CarrinhoItem> {
            override fun onResponse(call: Call<CarrinhoItem>, response: Response<CarrinhoItem>) {
                if(response.isSuccessful){

                    val carrinho = response.body()

                    if (carrinho != null) {
                        atualizaCarrinho()
                                            }

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

            override fun onFailure(call: Call<CarrinhoItem>, t: Throwable) {
                CarregaOff()
                Snackbar.make(binding.containerCarrinho,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).carrinho.add(id).enqueue(callback)

    }

    fun removeProduto(id: Int){

        val callback = object : Callback<CarrinhoItem> {
            override fun onResponse(call: Call<CarrinhoItem>, response: Response<CarrinhoItem>) {
                if(response.isSuccessful){

                    val carrinho = response.body()

                    if (carrinho != null) {
                        atualizaCarrinho()
                    }

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

            override fun onFailure(call: Call<CarrinhoItem>, t: Throwable) {
                Snackbar.make(binding.containerCarrinho,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).carrinho.remove(id).enqueue(callback)

    }

    fun deleteProduto(id: Int){

        val callback = object : Callback<CarrinhoItem> {
            override fun onResponse(call: Call<CarrinhoItem>, response: Response<CarrinhoItem>) {
                if(response.isSuccessful){

                    val carrinho = response.body()

                    if (carrinho != null) {
                        atualizaCarrinho()
                    }

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

            override fun onFailure(call: Call<CarrinhoItem>, t: Throwable) {
                Snackbar.make(binding.containerCarrinho,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).carrinho.delete(id).enqueue(callback)
    }

    fun CarregaOn(){
        binding.swipperCarrinho.isRefreshing = true
        binding.containerCarrinho.visibility = View.GONE
    }

    fun CarregaOff() {
        binding.swipperCarrinho.isRefreshing = false
        binding.containerCarrinho.visibility = View.VISIBLE

    }



}