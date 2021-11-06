package com.example.netuno.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.netuno.API.API
import com.example.netuno.R
import com.example.netuno.databinding.FragmentListaPerfilBinding
import com.example.netuno.databinding.FragmentListaProdutosBinding
import com.example.netuno.databinding.ProdutoCardBinding
import com.example.netuno.databinding.ProdutoListCardBinding
import com.example.netuno.model.Categoria
import com.example.netuno.model.Produto
import com.example.netuno.ui.formataNumero
import com.example.netuno.ui.montaShimmerPicaso
import com.example.netuno.ui.msg
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val CATEGORIA_ID = "id"
private const val CATEGORIA_NOME = "nome"

class ListaProdutosFragment : Fragment() {
    lateinit var  binding: FragmentListaProdutosBinding
    lateinit var containerPro: ViewGroup
    lateinit var ctx: Context

    //Parâmetros
    private var catId: Int? = null
    private var catNome: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            catId = it.getInt(CATEGORIA_ID)
            catNome = it.getString(CATEGORIA_NOME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaProdutosBinding.inflate(layoutInflater)

        if (container != null) {
            ctx = container.context
        }

        if (container != null) {
            containerPro = container
        }

        //Setando o nome da categoria
        binding.lblCatLista.text = catNome

        //Carrega os produtos da categoria
        addListaProdutos()

        binding.swipperListProd.setOnRefreshListener {
            addListaProdutos()
        }


        return binding.root
    }

    private fun addListaProdutos() {

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                CarregaOff()
                if(response.isSuccessful){
                    val produtos = response.body()
                    atualizarUI(produtos)
                }else{
                    msg(binding.containerListProd,"Não é possível atualizar os produtos")
                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                CarregaOff()
                msg(binding.containerListProd,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)
            }

        }
        if (catId == null){
            API(ctx).produto.index().enqueue(callback)
        }else{
            API(ctx).categoria.produtos(catId!!).enqueue(callback)
        }
        CarregaOn()

    }

    fun atualizarUI(produtos: List<Produto>?) {
        binding.containerListProd.removeAllViews()

        if (produtos != null) {
            if(produtos.isEmpty()){
                binding.lblSemProd.visibility = View.VISIBLE
            }
        }

        produtos?.forEach{
            val cardBinding = ProdutoListCardBinding.inflate(layoutInflater)

            cardBinding.lblNomeProdList.text = it.ds_nome
            cardBinding.lblPrecoProdList.text = "R$ ${formataNumero(it.vl_produto, "dinheiro")}"

            //Montando o shimmer para o picaso usar
            var sDrawable = montaShimmerPicaso()

            if(it.ds_linkfoto.isNotEmpty()){
                Picasso.get()
                    .load(it.ds_linkfoto)
                    .placeholder(sDrawable)
                    .error(R.drawable.no_image)
                    .into(cardBinding.imgProdutoList)
            }

            var prodId = it.id

            cardBinding.card.setOnClickListener{
                containerPro?.let {
                    parentFragmentManager.beginTransaction().replace(it.id,  ProdutoDescFragment.newInstance(prodId))
                        .addToBackStack("ListaCategoria").commit()
                }
            }
            binding.containerListProd.addView(cardBinding.root)


        }


    }

    fun CarregaOn(){
        binding.shimmerListProd.visibility = View.VISIBLE
        binding.shimmerListProd.startShimmer()
        binding.swipperListProd.isRefreshing = true
    }

    fun CarregaOff(){
        binding.shimmerListProd.visibility = View.GONE
        binding.shimmerListProd.stopShimmer()
        binding.swipperListProd.isRefreshing = false
    }


    companion object{
        @JvmStatic
        fun newInstance(id: Int, nome: String) =
            ListaProdutosFragment().apply {
                arguments = Bundle().apply {
                    putInt(CATEGORIA_ID, id)
                    putString(CATEGORIA_NOME, nome)
                }
            }
    }


}