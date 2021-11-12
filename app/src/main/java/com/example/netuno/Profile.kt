package com.example.netuno

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.netuno.API.API
import com.example.netuno.activitys.MainActivity
import com.example.netuno.databinding.ActivityProfileBinding
import com.example.netuno.model.Cliente
import com.example.netuno.model.Endereco
import com.example.netuno.model.User
import com.example.netuno.ui.*
import com.example.netuno.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.R.drawable

import android.graphics.drawable.BitmapDrawable




class Profile : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding

    //Id do cliente
    var clienteId: Int = 0
    var enderecoId: Int = 0
    var ctx: Context = this
    lateinit var img: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val imgFoto: Uri? = it.data?.data
                if (imgFoto != null) {
                    val bitmap =  MediaStore.Images.Media.getBitmap(this.contentResolver, imgFoto)
                    binding.imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
                    binding.imageView.setImageBitmap(bitmap)
                    if (bitmap != null) {
                        img = bitmap
                    }
                }
            }
        }

        binding.fabFoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            //startActivity(i)

            launcher.launch(i)

        }

        clienteId = retornaClienteId(this)

        //Verificando se é uma conta nova ou alteração
        if (clienteId != 0) {
            verificaLogin()
            pegaCliente()
        } else {
            CarregaOff()
            atuUiNovo()
        }

        binding.fabSalva.setOnClickListener {

            if (clienteId != 0) {
                updateCliente()
            } else {
                criaUsuario()
            }

        }




    }

    override fun onResume() {
        super.onResume()

        clienteId = retornaClienteId(this)

        //Verificando se é uma conta nova ou alteração
        if (clienteId != 0) {
            verificaLogin()
            //pegaCliente()
        } else {
            CarregaOff()
            atuUiNovo()
        }


    }

    fun chamaLogin() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    fun verificaLogin() {
        // Se não tiver logado, vai para o login
        var token = retornaToken(this)
        if (token == "") {
            chamaLogin()
        }
    }

    fun CarregaOn() {
        binding.rowProgress.visibility = View.VISIBLE
        binding.rowDados.visibility = View.INVISIBLE
    }

    fun CarregaOff() {
        binding.rowProgress.visibility = View.GONE
        binding.rowDados.visibility = View.VISIBLE
    }

    fun pegaCliente() {

        val callback = object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {


                if (response.isSuccessful) {

                    val cliente = response.body()
                    if (cliente != null) {
                        atualizarUI(cliente)
                    }

                } else {

                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível carregar os dados do cliente")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                CarregaOff()
                //msg(binding.usernameView, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        if (clienteId != null) {
            API(this).cliente.show(clienteId!!).enqueue(callback)
            CarregaOn()
        }
    }

    fun atualizarUI(cliente: List<Cliente>) {

        cliente.forEach {
            binding.usernameView.text = it.ds_nome
            binding.editTextTextPersonName.setText(it.ds_nome)
            binding.editTextTextPersonCPF.setText(it.ds_cpf)
            binding.editTextPhone.setText(it.ds_celular)
            binding.editTextTextEmailAddress.setText(it.ds_email)
            binding.editTextTextEmailAddress.isEnabled = false
            if(it.ds_fotoperfil != ""){
                binding.imageView.setImageBitmap(Base64toImg(it.ds_fotoperfil))
            }

            pegaEndereco(it.id)
        }

    }

    fun pegaEndereco(id: Int) {

        val callback = object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                CarregaOff()
                if (response.isSuccessful) {

                    val endereco = response.body()
                    if (endereco != null) {
                        atualizarEndereco(endereco)
                        enderecoId = endereco.id
                    }

                } else {

                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível carregar os dados do endereço")
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
        API(this).cliente.enderecos(id).enqueue(callback)
    }

    fun atualizarEndereco(endereco: Endereco) {

        binding.editTextNumberSignedCEP.setText(endereco.ds_cep)
        binding.editTextTextPostalAddress.setText(endereco.ds_endereco)
        binding.editTexAddresstNumber.setText(endereco.ds_numero)
        binding.editTextAddressComplement.setText(endereco.ds_complemento)
        binding.editTextTextAddressCity.setText(endereco.ds_cidade)
        binding.editTextAdressState.setText(endereco.ds_uf)
    }

    fun updateCliente() {

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {

                //CarregaOff()
                if (response.isSuccessful) {
                    updateEndereco()
                } else {

                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível atualizar os dados do cliente")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                //CarregaOff()
                msg(binding.usernameView, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }

        if (binding.editTextTextPassword.text.toString() == "") {
            msg(binding.usernameView, "Preencha a senha para continuar")
            binding.editTextTextPassword.requestFocus()
        } else {
            if (clienteId != null) {
                var cliente = Cliente(
                    binding.editTextTextPersonName.text.toString(),
                    "",
                    0,
                    "",
                    binding.editTextPhone.text.toString(),
                    clienteId,
                    binding.editTextTextPersonCPF.text.toString(),
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextTextPassword.text.toString(),
                    if(img == null){""}else{imgToBase64(img)}
                )

                API(this).cliente.update(cliente, clienteId).enqueue(callback)
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

    fun atuUiNovo() {
        binding.usernameView.text = "Novo usuário"
        binding.fabSalva.text = "Criar conta"
    }

    fun criaUsuario() {

        val callback = object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                //CarregaOff()
                if (response.isSuccessful) {

                    val user = response.body()
                    if (user != null) {
                        criaCliente(user)
                    }

                } else {
                    CarregaOff()
                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível criar o usuário")
                    }

                    Log.e("ERROR", response.code().toString())
                    Log.e("ERROR", response.body().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                CarregaOff()
                msg(binding.usernameView, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }

        var user = User(
            binding.editTextTextEmailAddress.text.toString(),
            binding.editTextTextPersonName.text.toString(),
            "",
            "",
            0,
           "",
            "",
            binding.editTextTextPassword.text.toString(),
            ""
        )

        API(this).user.store(user).enqueue(callback)
        CarregaOn()


    }

    fun criaCliente(user: User) {

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {

                //CarregaOff()
                if (response.isSuccessful) {

                    val cliente = response.body()
                    if (cliente != null) {
                        criaEndereco(cliente)
                    }

                } else {
                    CarregaOff()
                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível criar o cliente")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                CarregaOff()
                msg(binding.usernameView, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }


        var cliente = Cliente(
            binding.editTextTextPersonName.text.toString(),
            "",
            user.id,
            "",
            binding.editTextPhone.text.toString(),
            0,
            binding.editTextTextPersonCPF.text.toString(),
            binding.editTextTextEmailAddress.text.toString(),
            binding.editTextTextPassword.text.toString(),
            if(img == null){""}else{imgToBase64(img)}
        )

        API(this).cliente.store(cliente).enqueue(callback)

    }

    fun criaEndereco(cliente: Cliente) {

        val callback = object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                if (response.isSuccessful) {
                    var alert = alertFun(
                        "Cadastro efetuado!",
                        "Faça o login para continuar a navegação :)",
                        ctx
                    )

                    if (alert != null) {
                        alert.setOnDismissListener {
                            chamaLogin()
                        }
                        alert.create().show()
                    }

                    CarregaOff()


                } else {
                    CarregaOff()
                    if (response.code() == 401) {
                        chamaLogin()
                    } else {
                        msg(binding.usernameView, "Não é possível criar o endereço")
                    }

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                CarregaOff()
                msg(binding.usernameView, "Não é possível se conectar ao servidor")
                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }


        var endereco = Endereco(
            "N/A",
            "",
            binding.editTexAddresstNumber.text.toString(),
            binding.editTextAdressState.text.toString(),
            "",
            0,
            binding.editTextNumberSignedCEP.text.toString(),
            binding.editTextTextAddressCity.text.toString(),
            binding.editTextAddressComplement.text.toString(),
            cliente.id,
            binding.editTextTextPostalAddress.text.toString()
        )

        API(this).endereco.store(endereco).enqueue(callback)

    }

    fun chamaHome(){
        startActivity(Intent(this, MainActivity::class.java))
    }


}