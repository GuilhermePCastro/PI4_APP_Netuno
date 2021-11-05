package com.example.netuno.ui.login

import android.app.Activity
import android.content.Context
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
import com.example.netuno.databinding.ActivityLoginBinding

import com.example.netuno.R
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

            login.setOnClickListener {

                val callback = object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
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
                        Snackbar.make(it,"Não é possível se conectar ao servidor",
                            Snackbar.LENGTH_LONG).show()

                        Log.e("ERROR", "Falha ao executar serviço", t)

                    }

                }
                var user = User("","","",0, 0, "admin@gmail.com","Android", "123456789","")
                API(this@LoginActivity).user.login(user).enqueue(callback)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = "Olá"
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
                if(response.isSuccessful){

                    val user = response.body()

                    if (user != null) {
                        Snackbar.make(binding.login,"Olá ${user.name}",
                            Snackbar.LENGTH_LONG).show()
                    }

                }else{
                    Snackbar.make(binding.login,"Erro ao buscar informações do usuário",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(binding.login,"Não é possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)

            }

        }
        API(this).user.show(token).enqueue(callback)
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