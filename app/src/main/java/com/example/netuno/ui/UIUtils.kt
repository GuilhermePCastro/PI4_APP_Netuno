package com.example.netuno.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

fun alert (titulo: String, msg: String, ctx: Context){

    AlertDialog.Builder(ctx)
        .setTitle(titulo)
        .setMessage(msg)
        .setPositiveButton("OK",null)
        .create()
        .show()
}

fun montaShimmerPicaso(): Drawable{

    val s = Shimmer.ColorHighlightBuilder()
        .setAutoStart(true)
        .setDuration(1000)
        .setBaseColor(Color.GRAY)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setHighlightColor(Color.WHITE)
        .setBaseAlpha(0.8f)
        .build()

    val sDrawable = ShimmerDrawable()
    sDrawable.setShimmer(s)

    return sDrawable
}

fun formataNumero(n: Double, formato: String): String {

    var retorno: String
    retorno = n.toString()

    if (formato == "dinheiro"){
        var d = "%,3.2f".format(n)
        d = d.replace('.', 'p')
        d = d.replace(',', 'v')
        d = d.replace('p', ',')
        d = d.replace('v', '.')
        retorno = d
    }


    return retorno
}

fun retornaToken(ctx: Context): String? {

    val p = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val token = p.getString("token", "")

    return token

}