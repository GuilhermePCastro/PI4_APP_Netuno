package com.example.netuno.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.R
import com.example.netuno.databinding.CardPedidoBinding
import com.example.netuno.databinding.FragmentHistoricoCompraBinding
import com.example.netuno.databinding.FragmentListaPerfilBinding
import com.example.netuno.databinding.ProdutoCardBinding

class HistoricoCompraFragment : Fragment() {
    lateinit var  binding: FragmentHistoricoCompraBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoricoCompraBinding.inflate(inflater)

        val pedido = CardPedidoBinding.inflate((layoutInflater))
        pedido.lblPedido.text = "Pedido #123"
        pedido.lblData.text = "28/10/2021"
        pedido.lblEnviado.text = "Status: Em andamento"
        pedido.lblTotPedido.text = "Total: R$ 999,99"
        pedido.lblTotItens.text = "Total Itens: 2"
        binding.containerHistCompra.addView(pedido.root)

        val pedido2 = CardPedidoBinding.inflate((layoutInflater))
        pedido2.lblPedido.text = "Pedido #124"
        pedido2.lblData.text = "28/10/2021"
        pedido2.lblEnviado.text = "Status: Em andamento"
        pedido2.lblTotPedido.text = "Total: R$ 159,99"
        pedido2.lblTotItens.text = "Total Itens: 1"
        binding.containerHistCompra.addView(pedido2.root)

        return binding.root
    }


}