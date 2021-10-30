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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        if (container != null) {
            ctx = container.context
        }

        atualizarDestaques()

        return binding.root
    }

    override fun onResume(){
        super.onResume()

        atualizarDestaques()
    }

    fun atualizarDestaques(){

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {

                if(response.isSuccessful){
                    val produtos = response.body()
                    atualizarUI(produtos)
                }else{
                    Snackbar.make(binding.conDestaques,"Não é possível atualizar os destaques",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Snackbar.make(binding.conDestaques,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(ctx).produto.index().enqueue(callback)


    }

    fun atualizarUI(lista: List<Produto>?){
        binding.conDestaques.removeAllViews()

        lista?.forEach{
            val cardBinding = ProdutoCardBinding.inflate(layoutInflater)

            cardBinding.lblNomeProdutoCard.text = it.ds_nome
            cardBinding.lblPrecoProdutoCard.text = it.vl_produto.toString()

            //Montando o shimmer para o picaso usar
            val s = Shimmer.ColorHighlightBuilder()
                .setAutoStart(true)
                .setDuration(1000)
                .setBaseColor(Color.GRAY)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setHighlightColor(Color.WHITE)
                .setBaseAlpha(0.8f)
                .build()

            val sDrawable = ShimmerDrawable()
            sDrawable.setShimmer(s)

            Picasso.get()
                .load("https://rockcontent.com/br/wp-content/uploads/sites/2/2019/03/tamanho-imagem-blog.png")
                .placeholder(sDrawable)
                .error(R.drawable.no_image)
                .into(cardBinding.imageView3)

            binding.conDestaques.addView(cardBinding.root)
        }
    }




}