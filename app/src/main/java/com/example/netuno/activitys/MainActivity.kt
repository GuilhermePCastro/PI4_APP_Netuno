package com.example.netuno.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.netuno.R
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.fragments.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_baseline_account_circle_24)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.perfil -> supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, ListaPerfilFragment()).addToBackStack("fragHome").commit()
                R.id.home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, HomeFragment()).addToBackStack("fragHome").commit()
                R.id.categoria -> supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, CategoriasFragment()).addToBackStack("fragHome").commit()
            }

            true
        }

        binding.bottomNav.selectedItemId = R.id.home


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.carrinho -> {
                supportFragmentManager.beginTransaction().replace(R.id.containerHistCompra, CarrinhoFragment()).addToBackStack("fragHome").commit()

            }
        }
        return super.onOptionsItemSelected(item)
    }

}