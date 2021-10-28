package com.example.netuno.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginRight
import com.example.netuno.R
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.databinding.FragmentHomeBinding
import com.example.netuno.databinding.ProdutoCardBinding
import kotlin.math.absoluteValue

class HomeFragment : Fragment() {
    lateinit var  binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)


        val cardProduto = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto.lblNomeProdutoCard.text = "Produto 1"
        cardProduto.lblPrecoProdutoCard.text = "R$ 99,99"
        binding.conDestaques.addView(cardProduto.root)

        val cardProduto2 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto2.lblNomeProdutoCard.text = "Produto 2"
        cardProduto2.lblPrecoProdutoCard.text = "R$ 299,99"
        binding.conDestaques.addView(cardProduto2.root)

        val cardProduto3 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto3.lblNomeProdutoCard.text = "Produto 3"
        cardProduto3.lblPrecoProdutoCard.text = "R$ 499,99"
        binding.conDestaques.addView(cardProduto3.root)

        val cardProduto4 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto4.lblNomeProdutoCard.text = "Produto 4"
        cardProduto4.lblPrecoProdutoCard.text = "R$ 999,99"
        binding.conDestaques.addView(cardProduto4.root)


        val cardProduto5 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto5.lblNomeProdutoCard.text = "Produto 1"
        cardProduto5.lblPrecoProdutoCard.text = "R$ 99,99"
        binding.conLancamentos.addView(cardProduto5.root)

        val cardProduto6 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto6.lblNomeProdutoCard.text = "Produto 2"
        cardProduto6.lblPrecoProdutoCard.text = "R$ 299,99"
        binding.conLancamentos.addView(cardProduto6.root)

        val cardProduto7 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto7.lblNomeProdutoCard.text = "Produto 3"
        cardProduto7.lblPrecoProdutoCard.text = "R$ 499,99"
        binding.conLancamentos.addView(cardProduto7.root)

        val cardProduto8 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto8.lblNomeProdutoCard.text = "Produto 4"
        cardProduto8.lblPrecoProdutoCard.text = "R$ 999,99"
        binding.conLancamentos.addView(cardProduto8.root)

        return binding.root
    }




}