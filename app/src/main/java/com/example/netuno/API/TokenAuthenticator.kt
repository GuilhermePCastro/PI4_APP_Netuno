package com.example.netuno.API

import android.content.Context
import com.example.netuno.model.User
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Call

class TokenAuthenticator(val ctx: Context): Authenticator {

    private lateinit var userLogin: User

    override fun authenticate(route: Route?, response: Response): Request? {

        val p = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = p.getString("token", "")

        return response.request().newBuilder()
            .header("Autorization", "Bearer ${token}")
            .build()

    }

}