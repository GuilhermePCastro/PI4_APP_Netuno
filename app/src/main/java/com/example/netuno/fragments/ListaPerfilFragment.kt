package com.example.netuno.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netuno.Profile
import com.example.netuno.R
import com.example.netuno.activitys.MainActivity
import com.example.netuno.databinding.FragmentListaPerfilBinding
import com.example.netuno.ui.login.LoginActivity

class ListaPerfilFragment : Fragment() {
    lateinit var  binding: FragmentListaPerfilBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentListaPerfilBinding.inflate(inflater)

        binding.cardPedidos.setOnClickListener {
            container?.let{
                parentFragmentManager.beginTransaction().replace(it.id, HistoricoCompraFragment()).addToBackStack("listaPerfil").commit()
            }
        }

        binding.cardDados.setOnClickListener {
            container?.let{
                val i = Intent(container.context, Profile::class.java)
                startActivity(i)

            }
        }

        return binding.root
    }


}