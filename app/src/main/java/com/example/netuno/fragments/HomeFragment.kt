package com.example.netuno.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.R
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.databinding.FragmentHomeBinding
import com.example.netuno.databinding.ProdutoCardBinding

class HomeFragment : Fragment() {
    lateinit var  binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)

        /*
        val cardProduto = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto.lblNomeProdutoCard.text = "Produto 1"
        cardProduto.lblPrecoProdutoCard.text = "R$ 99,99"
        binding.conDestaques.addView(cardProduto.root)

        val cardProduto2 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto.lblNomeProdutoCard.text = "Produto 2"
        cardProduto.lblPrecoProdutoCard.text = "R$ 299,99"
        binding.conDestaques.addView(cardProduto2.root)

        val cardProduto3 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto.lblNomeProdutoCard.text = "Produto 3"
        cardProduto.lblPrecoProdutoCard.text = "R$ 499,99"
        binding.conLancamentos.addView(cardProduto3.root)*/

        val cardProduto4 = ProdutoCardBinding.inflate((layoutInflater))
        cardProduto4.lblNomeProdutoCard.text = "Produto 4"
        cardProduto4.lblPrecoProdutoCard.text = "R$ 999,99"
        binding.conLancamentos.addView(cardProduto4.root)

        return binding.root
    }




}