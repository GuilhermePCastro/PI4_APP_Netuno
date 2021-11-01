package com.example.netuno.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginRight
import com.example.netuno.API.API
import com.example.netuno.R
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.databinding.FragmentHomeBinding
import com.example.netuno.databinding.ProdutoCardBinding
import com.example.netuno.model.Produto
import com.example.netuno.ui.formataNumero
import com.example.netuno.ui.montaShimmerPicaso
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue

class HomeFragment : Fragment() {
    lateinit var  binding: FragmentHomeBinding

    lateinit var ctx: Context
    lateinit var containerFrag: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        if (container != null) {
            ctx = container.context
        }

        if (container != null) {
            containerFrag = container
        }

        binding.swiperHome.setOnRefreshListener {
            atualizarDestaques()
            atualizarLancamentos()
        }

        atualizarDestaques()
        atualizarLancamentos()

        return binding.root
    }

    override fun onResume(){
        super.onResume()

        atualizarDestaques()
        atualizarLancamentos()
    }

    fun atualizarDestaques(){

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                CarregaOff()
                if(response.isSuccessful){
                    val produtos = response.body()
                    atualizarUIDestaques(produtos)
                }else{
                    Snackbar.make(binding.conDestaques,"Não é possível atualizar os destaques",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                CarregaOff()
                Snackbar.make(binding.conDestaques,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).produto.destaques().enqueue(callback)
        CarregaOn()


    }

    fun atualizarUIDestaques(lista: List<Produto>?){
        binding.conDestaques.removeAllViews()

        lista?.forEach{
            val cardBinding = ProdutoCardBinding.inflate(layoutInflater)

            cardBinding.lblNomeProdutoCard.text = it.ds_nome
            cardBinding.lblPrecoProdutoCard.text = "R$ ${formataNumero(it.vl_produto, "dinheiro")}"
            cardBinding.lblcategoriaProd.text = it.categoria.ds_categoria

            //Montando o shimmer para o picaso usar
            var sDrawable = montaShimmerPicaso()

            if(it.ds_linkfoto.isNotEmpty()){
                Picasso.get()
                    .load(it.ds_linkfoto)
                    .placeholder(sDrawable)
                    .error(R.drawable.no_image)
                    .into(cardBinding.imgProdutoHome)
            }

            var prodId = it.id

            cardBinding.card.setOnClickListener{
                containerFrag?.let {
                    parentFragmentManager.beginTransaction().replace(it.id, ProdutoDescFragment.newInstance(prodId))
                        .addToBackStack("Home").commit()
                }
            }
            binding.conDestaques.addView(cardBinding.root)

        }
    }

    fun atualizarLancamentos(){

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                CarregaOff()

                if(response.isSuccessful){
                    val produtos = response.body()
                    atualizarUILancamentos(produtos)
                }else{
                    Snackbar.make(binding.conLancamentos,"Não é possível atualizar os lançamentos",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                CarregaOff()
                Snackbar.make(binding.conLancamentos,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).produto.lancamentos().enqueue(callback)
        CarregaOn()


    }

    fun atualizarUILancamentos(lista: List<Produto>?){
        binding.conLancamentos.removeAllViews()

        lista?.forEach{
            val cardBinding = ProdutoCardBinding.inflate(layoutInflater)

            cardBinding.lblNomeProdutoCard.text = it.ds_nome
            cardBinding.lblPrecoProdutoCard.text = "R$ ${formataNumero(it.vl_produto, "dinheiro")}"
            cardBinding.lblcategoriaProd.text = it.categoria.ds_categoria

            var sDrawable = montaShimmerPicaso()

            if(it.ds_linkfoto.isNotEmpty()){
                Picasso.get()
                    .load(it.ds_linkfoto)
                    .placeholder(sDrawable)
                    .error(R.drawable.no_image)
                    .into(cardBinding.imgProdutoHome)
            }

            cardBinding.card.setOnClickListener{
                containerFrag?.let {
                    parentFragmentManager.beginTransaction().replace(it.id, ProdutoDescFragment())
                        .addToBackStack("Home").commit()
                }
            }

            binding.conLancamentos.addView(cardBinding.root)
        }
    }

    fun CarregaOn(){
        binding.shimmerDes.visibility = View.VISIBLE
        binding.shimmerLan.visibility = View.VISIBLE
        binding.shimmerDes.startShimmer()
        binding.shimmerDes.startShimmer()
        binding.swiperHome.isRefreshing = true
    }

    fun CarregaOff(){
        binding.shimmerDes.visibility = View.GONE
        binding.shimmerLan.visibility = View.GONE
        binding.shimmerDes.stopShimmer()
        binding.shimmerDes.stopShimmer()
        binding.swiperHome.isRefreshing = false
    }




}