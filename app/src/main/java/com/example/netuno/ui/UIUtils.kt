package com.example.netuno.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.netuno.activitys.MainActivity
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar
import android.graphics.Bitmap

import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.Exception
import android.R
import android.net.Uri
import androidx.core.graphics.drawable.toDrawable


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

fun formataData(d: String): String {

    //Simple dateformat
    var retorno: String
    var dia = d.substring(8,10)
    var mes = d.substring(5,7)
    var ano = d.substring(0,4)

    retorno = "${dia}/${mes}/${ano}"

    return retorno
}

fun retornaToken(ctx: Context): String? {

    val p = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val token = p.getString("token", "")

    return token

}

fun retornaUserId(ctx: Context): Int? {

    val p = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val id = p.getInt("user_id", 0)

    return id

}

fun retornaClienteId(ctx: Context): Int {

    val p = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val id = p.getInt("cliente_id", 0)

    return id

}

fun msg(view: View, msg: String){

    Snackbar.make(view,msg,Snackbar.LENGTH_LONG).show()
}

fun alertFun (titulo: String, msg: String, ctx: Context): AlertDialog.Builder? {

    return AlertDialog.Builder(ctx)
        .setTitle(titulo)
        .setMessage(msg)
        .setPositiveButton("OK",null)

}


fun imgToBase64(img: Bitmap?): String {

    var bos = ByteArrayOutputStream()
    if (img != null) {
        img.compress(Bitmap.CompressFormat.JPEG, 10, bos)
    }
    var encodeString = Base64.encodeToString(bos.toByteArray(), Base64.NO_PADDING);

    return encodeString
}

fun Base64toImg(string: String): Bitmap {

    var decodeString = Base64.decode(string, Base64.NO_PADDING)
    return BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
}