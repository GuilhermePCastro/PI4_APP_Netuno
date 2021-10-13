package com.example.netuno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.netuno.databinding.ActivityMenuBinding

class Menu : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração de menu sanduíche
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toogle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.abrir_menu, R.string.fechar_menu)

        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {

            when(it.itemId.toString()) {
                "menu_orders" -> {
                    //val frag = nomeDoFragmento.newInstance()
                    //supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
                "menu_home" -> {
                    //val frag = nomeDoFragmento.newInstance()
                    //supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
                "menu_categories" -> {
                    //val frag = nomeDoFragmento.newInstance()
                    //supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
                "menu_qrcode" -> {
                    //val frag = nomeDoFragmento.newInstance()
                    //supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
            }

            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toogle.onOptionsItemSelected(item)
    }
}