package com.example.netuno.fragments

import android.content.Context
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
import com.example.netuno.ui.retornaToken

class ListaPerfilFragment : Fragment() {
    lateinit var  binding: FragmentListaPerfilBinding
    lateinit var ctx: Context
    lateinit var containerFrag: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentListaPerfilBinding.inflate(inflater)

        if (container != null) {
            ctx = container.context
        }
        if (container != null) {
            containerFrag = container
        }

        // Se não tiver logado, vai para o login
        var token = retornaToken(ctx)
        if(token == ""){
            container?.let{
                val i = Intent(container.context, LoginActivity::class.java)
                startActivity(i)
            }
        }

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

        binding.cardSair.setOnClickListener {
            val p = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val editP = p.edit()
            editP.putString("token", "")
            editP.commit()

            container?.let{
                val i = Intent(container.context, LoginActivity::class.java)
                startActivity(i)
            }

        }

        return binding.root
    }


}