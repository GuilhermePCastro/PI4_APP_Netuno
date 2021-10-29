package com.example.netuno.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.R
import com.example.netuno.databinding.CardCategoriaBinding
import com.example.netuno.databinding.FragmentCategoriasBinding
import com.example.netuno.databinding.FragmentHomeBinding
import com.example.netuno.databinding.ProdutoCardBinding


class CategoriasFragment : Fragment() {
    lateinit var  binding: FragmentCategoriasBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasBinding.inflate(inflater)

        val categoria = CardCategoriaBinding.inflate((layoutInflater))
        categoria.lblCategoriaLista.text = "Camisas"
        binding.containerCategorias.addView(categoria.root)

        val categoria2 = CardCategoriaBinding.inflate((layoutInflater))
        categoria2.lblCategoriaLista.text = "Colecionáveis"
        binding.containerCategorias.addView(categoria2.root)

        val categoria3 = CardCategoriaBinding.inflate((layoutInflater))
        categoria3.lblCategoriaLista.text = "Estátuas"
        binding.containerCategorias.addView(categoria3.root)

        categoria.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ListaProdutosFragment()).addToBackStack("listaPerfil").commit()
            }
        }

        categoria2.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ListaProdutosFragment()).addToBackStack("listaPerfil").commit()
            }
        }

        categoria3.card.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, ListaProdutosFragment()).addToBackStack("listaPerfil").commit()
            }
        }


        return binding.root
    }

}