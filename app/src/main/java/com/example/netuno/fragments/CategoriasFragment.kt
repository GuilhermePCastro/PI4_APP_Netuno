package com.example.netuno.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.API.API
import com.example.netuno.R
import com.example.netuno.databinding.CardCategoriaBinding
import com.example.netuno.databinding.FragmentCategoriasBinding
import com.example.netuno.databinding.FragmentHomeBinding
import com.example.netuno.databinding.ProdutoCardBinding
import com.example.netuno.model.Categoria
import com.example.netuno.model.Produto
import com.example.netuno.ui.msg
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoriasFragment : Fragment() {
    lateinit var  binding: FragmentCategoriasBinding
    lateinit var containerCat: ViewGroup
    lateinit var ctx: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasBinding.inflate(inflater)


        if (container != null) {
            ctx = container.context
        }

        if (container != null) {
            containerCat = container
        }

        atualizarCategorias()

        binding.swipperListCat.setOnRefreshListener {
            atualizarCategorias()
        }

        return binding.root
    }


    override fun onResume(){
        super.onResume()

        atualizarCategorias()
    }

    fun atualizarCategorias(){

        val callback = object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                CarregaOff()
                if(response.isSuccessful){
                    val categorias = response.body()
                    atualizarUI(categorias)
                }else{
                    msg(binding.containerCategorias,"Não é possível atualizar as categorias")
                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                CarregaOff()
                msg(binding.containerCategorias,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).categoria.index().enqueue(callback)
        CarregaOn()

    }

    fun atualizarUI(lista: List<Categoria>?){
        binding.containerCategorias.removeAllViews()

        lista?.forEach{
            val cardBinding = CardCategoriaBinding.inflate(layoutInflater)

            cardBinding.lblCategoriaLista.text = it.ds_categoria

           var catId = it.id
           var catNome = it.ds_categoria


            cardBinding.card.setOnClickListener{
                containerCat?.let {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(it.id, ListaProdutosFragment.newInstance(catId, catNome))
                        .addToBackStack("ListaCategoria").commit()
                }
            }
            binding.containerCategorias.addView(cardBinding.root)


        }
    }

    fun CarregaOn(){
        binding.shimmerListCat.visibility = View.VISIBLE
        binding.shimmerListCat.startShimmer()
        binding.swipperListCat.isRefreshing = true
    }

    fun CarregaOff(){
        binding.shimmerListCat.visibility = View.GONE
        binding.shimmerListCat.stopShimmer()
        binding.swipperListCat.isRefreshing = false
    }



}