package com.example.netuno

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.netuno.API.API
import com.example.netuno.activitys.MainActivity
import com.example.netuno.databinding.ActivityProfileBinding
import com.example.netuno.model.Cliente
import com.example.netuno.model.Endereco
import com.example.netuno.ui.*
import com.example.netuno.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding

    //Id do cliente
    var clienteId: Int = 0
    var enderecoId: Int = 0
    var ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        verificaLogin()

        clienteId = retornaClienteId(this)

        //Verificando se é uma conta nova ou alteração
        if(clienteId != 0){
            pegaCliente()
        }

        binding.floatingActionButton2.setOnClickListener {

            if(clienteId != 0){
                updateCliente()
            }



        }



    }

    override fun onResume(){
        super.onResume()

        verificaLogin()
        clienteId = retornaClienteId(this)
        //Verificando se é uma conta nova ou alteração
        if(clienteId != 0){
            pegaCliente()
        }

    }

    fun chamaLogin() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    fun verificaLogin(){
        // Se não tiver logado, vai para o login
        var token = retornaToken(this)
        if(token == ""){
            chamaLogin()
        }
    }

    fun CarregaOn(){
        binding.rowProgress.visibility = View.VISIBLE
        binding.rowDados.visibility = View.INVISIBLE
    }

    fun CarregaOff(){
        binding.rowProgress.visibility = View.GONE
        binding.rowDados.visibility = View.VISIBLE
    }

    fun pegaCliente(){

        val callback = object : Callback<List<Cliente>> {
            override fun onResponse(call:  Call<List<Cliente>>, response: Response<List<Cliente>>) {

                CarregaOff()
                if(response.isSuccessful){

                    val cliente = response.body()
                    if (cliente != null) {
                        atualizarUI(cliente)
                    }

                }else{

                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        msg(binding.usernameView,"Não é possível carregar os dados do cliente")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call:  Call<List<Cliente>>, t: Throwable) {
                CarregaOff()
                msg(binding.usernameView,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        if (clienteId != null) {
            API(this).cliente.show(clienteId!!).enqueue(callback)
            CarregaOn()
        }
    }

    fun atualizarUI(cliente: List<Cliente>){

        cliente.forEach {
            binding.usernameView.text = it.ds_nome
            binding.editTextTextPersonName.setText(it.ds_nome)
            binding.editTextTextPersonCPF.setText(it.ds_cpf)
            binding.editTextPhone.setText(it.ds_celular)
            binding.editTextTextEmailAddress.setText(it.ds_email)
            binding.editTextTextEmailAddress.isEnabled = false

            pegaEndereco(it.id)
        }

    }

    fun pegaEndereco(id: Int){

        val callback = object : Callback<Endereco> {
            override fun onResponse(call:  Call<Endereco>, response: Response<Endereco>) {

                //CarregaOff()
                if(response.isSuccessful){

                    val endereco = response.body()
                    if (endereco != null) {
                        atualizarEndereco(endereco)
                        enderecoId = endereco.id
                    }

                }else{

                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        msg(binding.usernameView,"Não é possível carregar os dados do endereço")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call:  Call<Endereco>, t: Throwable) {
                //CarregaOff()
                msg(binding.usernameView,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(this).cliente.enderecos(id).enqueue(callback)
    }

    fun atualizarEndereco(endereco: Endereco){

        binding.editTextNumberSignedCEP.setText(endereco.ds_cep)
        binding.editTextTextPostalAddress.setText(endereco.ds_endereco)
        binding.editTexAddresstNumber.setText(endereco.ds_numero)
        binding.editTextAddressComplement.setText(endereco.ds_complemento)
        binding.editTextTextAddressCity.setText(endereco.ds_cidade)
        binding.editTextAdressState.setText(endereco.ds_uf)
    }

    fun updateCliente(){

        val callback = object : Callback<Cliente> {
            override fun onResponse(call:  Call<Cliente>, response: Response<Cliente>) {

                //CarregaOff()
                if(response.isSuccessful){
                    updateEndereco()
                }else{

                    if(response.code() == 401){
                        chamaLogin()
                    }else{
                        msg(binding.usernameView,"Não é possível atualizar os dados do cliente")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call:  Call<Cliente>, t: Throwable) {
                //CarregaOff()
                msg(binding.usernameView,"Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }

        if( binding.editTextTextPassword.text.toString() == ""){
            msg(binding.usernameView, "Preencha a senha para continuar")
            binding.editTextTextPassword.requestFocus()
        }else{
            if (clienteId != null) {
                var cliente = Cliente(  binding.editTextTextPersonName.text.toString(),
                    "",
                    0,
                    "",
                    binding.editTextPhone.text.toString(),
                    clienteId,
                    binding.editTextTextPersonCPF.text.toString(),
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextTextPassword.text.toString())

                API(this).cliente.update(cliente ,clienteId).enqueue(callback)
                //CarregaOn()
            }
        }


    }

    fun updateEndereco() {

        val callback = object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                //CarregaOff()
                if (response.isSuccessful) {
                   var alert = alertFun("Dados Pessoais", "Dados atualizados com sucesso!", ctx)

                    if (alert != null) {
                        alert.setOnDismissListener {
                            chamaHome()
                        }
                        alert.create().show()
                    }


                } else {

                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível atualizar os dados do cliente")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                //CarregaOff()
                msg(binding.usernameView, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }

        if (enderecoId != null) {
            var endereco = Endereco(
                "N/A",
                "",
                binding.editTexAddresstNumber.text.toString(),
                binding.editTextAdressState.text.toString(),
                "",
                enderecoId,
                binding.editTextNumberSignedCEP.text.toString(),
                binding.editTextTextAddressCity.text.toString(),
                binding.editTextAddressComplement.text.toString(),
                clienteId,
                binding.editTextTextPostalAddress.text.toString()
            )
            API(this).endereco.update(endereco, enderecoId).enqueue(callback)

        }
    }



    fun chamaHome(){
        startActivity(Intent(this, MainActivity::class.java))
    }

}