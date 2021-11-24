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
        var imgCon = Bitmap.createScaledBitmap(img, 150, 150, true )
        imgCon.compress(Bitmap.CompressFormat.JPEG, 20, bos)
    }
    var encodeString = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);

    return encodeString
}

fun Base64toImg(string: String): Bitmap {

    var decodeString = Base64.decode(string, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
}

//Função que calcula o valor do frete de acordo com um valor base e a diferença lógica entre os CEPs
fun CalculaFrete(cepDestino: Int): Double{

    var valorFrete  = 0.00
    var cepOrigem = 4696000
    //Diferença entre os CEPs
    var dif = cepOrigem - cepDestino

    //Deixando positivo o valor
    if(dif < 0){
        dif = dif * (-1)
    }

    valorFrete = ((dif/10000)/1.50).toDouble()

    //Se o frete for menor que 10, o frete é grátis
    if(valorFrete < 10){
        valorFrete = 0.00
    }

    return valorFrete
}