package com.example.netuno.activitys

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.example.netuno.R
import com.example.netuno.databinding.ActivityQrCodeBinding
import com.google.zxing.BarcodeFormat

class QrCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityQrCodeBinding
    lateinit var leitorQr: CodeScanner
    var permissaoCamera = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        verificarPermissaoCamera()


        setContentView(binding.root)
    }

    fun iniLeitorQR(){
        leitorQr = CodeScanner(this,binding.scannerView)

        leitorQr.camera = CodeScanner.CAMERA_BACK
        leitorQr.formats = listOf(BarcodeFormat.QR_CODE)
        leitorQr.isAutoFocusEnabled = true
        leitorQr.autoFocusMode = AutoFocusMode.SAFE
        leitorQr.isFlashEnabled = false
        leitorQr.scanMode = ScanMode.SINGLE

        //Se consegue ler o QRCode
        leitorQr.setDecodeCallback {
            runOnUiThread {
                val respI = Intent()
                respI.putExtra("qrcode", it.text)
                setResult(RESULT_OK, respI)
                finish()
            }
        }

        leitorQr.setErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Não foi possível abriar a camera", Toast.LENGTH_LONG).show()
                Log.e("QrCodeActivity" , "IniciaQR", it)
                setResult(RESULT_CANCELED)
                finish()
            }
        }

        leitorQr.startPreview()
    }

    fun verificarPermissaoCamera(){

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA), 1)
        }else{
            permissaoCamera = true
            iniLeitorQR()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissaoCamera = true
                iniLeitorQR()
            }else{
                permissaoCamera = false
                Toast.makeText(this, "Sem permissão, sem QRCode", Toast.LENGTH_LONG).show()
                setResult(RESULT_CANCELED)
                finish()
            }
        }

    }

    private fun mostrarDialogoPermissaoCamera() {
        AlertDialog.Builder(this)
            .setTitle("Permissão da câmera")
            .setMessage("Habilite a permissão do uso da câmera em configurações para pdoer ler o QRCode")
            .setCancelable(false)
            .setPositiveButton("Configurações") { dialogInterface, i ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
                setResult(RESULT_CANCELED)
                finish()
            }
            .setNegativeButton("Cancelar") { dialogInterface, i ->
                setResult(RESULT_CANCELED)
                finish()
            }
    }

    override fun onResume() {
        super.onResume()

        if (permissaoCamera){
            leitorQr.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()

        if(permissaoCamera){
            leitorQr.releaseResources()
        }
    }
}