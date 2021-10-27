package com.example.netuno.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.netuno.R
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.fragments.CarrinhoFragment
import com.example.netuno.fragments.HistoricoCompraFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.perfil -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HistoricoCompraFragment()).commit()
            }

            true
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.carrinho -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, CarrinhoFragment()).commit()

            }
        }
        return super.onOptionsItemSelected(item)
    }

}