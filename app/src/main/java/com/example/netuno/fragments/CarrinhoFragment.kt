package com.example.netuno.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.R
import com.example.netuno.databinding.CardPedidoBinding
import com.example.netuno.databinding.FragmentCarrinhoBinding
import com.example.netuno.databinding.FragmentHistoricoCompraBinding
import com.example.netuno.databinding.ProdutoCarrinhoBinding

class CarrinhoFragment : Fragment() {
    lateinit var  binding: FragmentCarrinhoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCarrinhoBinding.inflate(inflater)

        val produto = ProdutoCarrinhoBinding.inflate((layoutInflater))
        produto.lblNomePro.text = "Produto"
        produto.lblValorTotProduto.text = "R$ 100,00"
        binding.containerCarrinho.addView(produto.root)

        val produto2 = ProdutoCarrinhoBinding.inflate((layoutInflater))
        produto2.lblNomePro.text = "Produto2"
        produto2.lblValorTotProduto.text = "R$ 100,00"
        binding.containerCarrinho.addView(produto2.root)

        return binding.root
    }


}