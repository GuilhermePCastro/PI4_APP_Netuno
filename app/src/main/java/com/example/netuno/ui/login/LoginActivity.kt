package com.example.netuno.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.netuno.API.API
import com.example.netuno.Profile
import com.example.netuno.databinding.ActivityLoginBinding

import com.example.netuno.R
import com.example.netuno.activitys.MainActivity
import com.example.netuno.fragments.HomeFragment
import com.example.netuno.model.Cliente
import com.example.netuno.model.Produto
import com.example.netuno.model.User
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login recuperarSenhaButton unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            binding.btnCriarConta?.setOnClickListener {
                val i = Intent(this@LoginActivity, Profile::class.java)
                startActivity(i)
            }

            login.setOnClickListener {

                val callback = object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        carregaOff()
                        if(response.isSuccessful){

                            val token = response.body()

                            if (token != null) {
                                val p = getSharedPreferences("auth", Context.MODE_PRIVATE)
                                val editP = p.edit()
                                editP.putString("token", token.token)
                                editP.commit()
                                pegaUsuario(token.token)
                            }

                        }else{
                            Snackbar.make(it,"Erro de credênciais!",
                                Snackbar.LENGTH_LONG).show()

                            Log.e("ERROR", response.errorBody().toString())
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        carregaOff()
                        Snackbar.make(it,"Não é possível se conectar ao servidor",
                            Snackbar.LENGTH_LONG).show()

                        Log.e("ERROR", "Falha ao executar serviço", t)

                    }

                }
                var login = binding.username.text.toString()
                var senha = binding.password.text.toString()

                var user = User(login,"","","", 0,"","Android", senha,"")
                API(this@LoginActivity).user.login(user).enqueue(callback)
                carregaOn()
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = "Bem vindo"
        val displayName = model.displayName

        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    fun pegaUsuario(token: String){

        val callback = object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                carregaOff()
                if(response.isSuccessful){

                    val user = response.body()

                    if (user != null) {
                        pegaCliente(user.id)
                    }

                }else{
                    Snackbar.make(binding.login,"Erro ao buscar informações do usuário",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                carregaOff()
                Snackbar.make(binding.login,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(this).user.show().enqueue(callback)
        carregaOn()
    }

    fun carregaOn(){
        binding.loading.visibility = View.VISIBLE
    }

    fun carregaOff(){
        binding.loading.visibility = View.GONE
    }

    fun pegaCliente(user: Int){

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                carregaOff()
                if(response.isSuccessful){

                    val cliente = response.body()

                    if (cliente != null) {
                        val p = getSharedPreferences("auth", Context.MODE_PRIVATE)
                        val editP = p.edit()
                        editP.putInt("cliente_id", cliente.id)
                        editP.putInt("user_id", cliente.user_id)
                        editP.commit()
                        loginViewModel.login(cliente.ds_nome, "")
                    }

                }else{
                    Snackbar.make(binding.login,"Erro ao buscar informações do usuário",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                carregaOff()
                Snackbar.make(binding.login,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(this).user.cliente(user).enqueue(callback)
        carregaOn()
    }

}



fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}