package com.example.netuno.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.netuno.R
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.fragments.*
import com.example.netuno.ui.login.LoginActivity
import com.example.netuno.ui.retornaToken

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo2)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.perfil -> {

                    var token = retornaToken(this)
                    if(token != ""){
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.containerHistCompra, ListaPerfilFragment()).addToBackStack("fragHome").commit()
                    }else{
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
                R.id.home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, HomeFragment()).addToBackStack("fragHome").commit()
                R.id.categoria -> supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, CategoriasFragment()).addToBackStack("fragHome").commit()
                R.id.qrcode ->{
                    startActivityForResult(Intent(this, QrCodeActivity::class.java), 1)
                }
                else -> supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, HomeFragment()).addToBackStack("fragHome").commit()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK && data != null ){
            val prodId = data.getStringExtra("qrcode")?.toInt()

            if(prodId != null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerHistCompra, ProdutoDescFragment.newInstance(prodId)).addToBackStack("fragHome").commit()
            }
        }
    }

}