package com.example.netuno.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.API.API
import com.example.netuno.R
import com.example.netuno.databinding.FragmentProdutoDescBinding
import com.example.netuno.model.Produto
import com.example.netuno.ui.formataNumero
import com.example.netuno.ui.montaShimmerPicaso
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PRODUTO_ID = "id"

class ProdutoDescFragment : Fragment() {

    lateinit var  binding: FragmentProdutoDescBinding
    lateinit var containerPro: ViewGroup
    lateinit var ctx: Context

    //Parâmetros
    private var prodId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            prodId = it.getInt(PRODUTO_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProdutoDescBinding.inflate(layoutInflater)

        if (container != null) {
            ctx = container.context
        }

        if (container != null) {
            containerPro = container
        }

        atulizaProduto()

        return binding.root
    }

    override fun onResume(){
        super.onResume()

        atulizaProduto()
    }

    fun atulizaProduto(){

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                CarregaOff()
                if(response.isSuccessful){
                    val produto = response.body()
                    if (produto != null) {
                        atualizarUI(produto)
                    }

                }else{
                    Snackbar.make(binding.scrollProdDesc,"Não é possível atualizar o produto",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                CarregaOff()
                Snackbar.make(binding.scrollProdDesc,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }

        if (prodId != null) {
            API(ctx).produto.show(prodId!!).enqueue(callback)
            CarregaOn()
        }


    }

    fun atualizarUI(produto: List<Produto>){

        produto?.forEach {

            binding.lblNome.text        = it.ds_nome
            binding.lblPreco.text       = "R$ ${formataNumero(it.vl_produto, "dinheiro")}"
            binding.lblDesc.text        = it.ds_descricao
            binding.lblDimensoes.text   = it.ds_dimensoes
            binding.lblMaterial.text    = it.ds_material
            binding.lblPeso.text        = it.ds_peso
            binding.lblCategoria.text   = it.categoria.ds_categoria
            binding.lblMarca.text       = it.marca.ds_marca

            var tags = ""
            it.tags.forEach {
                tags += it.ds_nome + " - "
            }

            tags = tags.substring(0, tags.length - 2)
            binding.lblCod.text = tags


            //Montando o shimmer para o picaso usar
            var sDrawable = montaShimmerPicaso()

            if(it.ds_linkfoto.isNotEmpty()){
                Picasso.get()
                    .load(it.ds_linkfoto)
                    .placeholder(sDrawable)
                    .error(R.drawable.no_image)
                    .into(binding.imageProduto)
            }

        }
    }

    fun CarregaOn(){
        binding.shimmerProdDesc.visibility = View.VISIBLE
        binding.shimmerProdDesc.startShimmer()
    }

    fun CarregaOff(){
        binding.shimmerProdDesc.visibility = View.GONE
        binding.shimmerProdDesc.stopShimmer()
    }

    companion object{
        @JvmStatic
        fun newInstance(id: Int) =
            ProdutoDescFragment().apply {
                arguments = Bundle().apply {
                    putInt(PRODUTO_ID, id)
                }
            }
    }


}