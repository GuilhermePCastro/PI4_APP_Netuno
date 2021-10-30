package com.example.netuno.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.R
import com.example.netuno.databinding.FragmentListaPerfilBinding
import com.example.netuno.databinding.FragmentListaProdutosBinding
import com.example.netuno.databinding.ProdutoCardBinding
import com.example.netuno.databinding.ProdutoListCardBinding

class ListaProdutosFragment : Fragment() {
    lateinit var  binding: FragmentListaProdutosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaProdutosBinding.inflate(layoutInflater)

        val cardProduto5 = ProdutoListCardBinding.inflate((layoutInflater))
        cardProduto5.lblNomeProdList.text = "Produto 1"
        cardProduto5.lblPrecoProdList.text = "R$ 99,99"
        binding.containerListProd.addView(cardProduto5.root)
        cardProduto5.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ProdutoDescFragment()).addToBackStack("listaPerfil").commit()
            }
        }

        val cardProduto6 = ProdutoListCardBinding.inflate((layoutInflater))
        cardProduto6.lblNomeProdList.text = "Produto 2"
        cardProduto6.lblPrecoProdList.text = "R$ 299,99"
        binding.containerListProd.addView(cardProduto6.root)
        cardProduto6.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ProdutoDescFragment()).addToBackStack("listaPerfil").commit()
            }
        }

        val cardProduto7 = ProdutoListCardBinding.inflate((layoutInflater))
        cardProduto7.lblNomeProdList.text = "Produto 3"
        cardProduto7.lblPrecoProdList.text = "R$ 499,99"
        binding.containerListProd.addView(cardProduto7.root)
        cardProduto7.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ProdutoDescFragment()).addToBackStack("listaPerfil").commit()
            }
        }

        val cardProduto8 = ProdutoListCardBinding.inflate((layoutInflater))
        cardProduto8.lblNomeProdList.text = "Produto 4"
        cardProduto8.lblPrecoProdList.text = "R$ 999,99"
        binding.containerListProd.addView(cardProduto8.root)
        cardProduto8.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ProdutoDescFragment()).addToBackStack("listaPerfil").commit()
            }
        }


        return binding.root
    }


}