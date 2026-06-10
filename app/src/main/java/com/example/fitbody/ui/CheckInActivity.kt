package com.example.fitbody.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.SimpleResponse
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckInActivity : AppCompatActivity() {

    private lateinit var btnCheckIn: Button
    private lateinit var txtLastCheckIn: TextView
    private lateinit var btnBack: TextView

    private val userId = 1

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openQRScanner()
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền camera để quét QR", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        btnCheckIn = findViewById(R.id.btnCheckIn)
        txtLastCheckIn = findViewById(R.id.txtLastCheckIn)
        btnBack = findViewById(R.id.btnBack)

        txtLastCheckIn.text = "Sẵn sàng quét mã QR để check-in"

        btnCheckIn.setOnClickListener {
            checkCameraPermission()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openQRScanner()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Quét mã QR phòng gym")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Đã hủy quét QR", Toast.LENGTH_SHORT).show()
            } else {
                sendCheckInToServer(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendCheckInToServer(qrCode: String) {
        txtLastCheckIn.text = "Đang gửi dữ liệu check-in..."

        RetrofitClient.instance.checkIn(userId, qrCode)
            .enqueue(object : Callback<SimpleResponse> {

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!

                        Toast.makeText(
                            this@CheckInActivity,
                            result.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (result.success) {
                            txtLastCheckIn.text = "Check-in thành công bằng QR:\n$qrCode"
                        } else {
                            txtLastCheckIn.text = "Check-in thất bại:\n${result.message}"
                        }
                    } else {
                        Toast.makeText(
                            this@CheckInActivity,
                            "Lỗi phản hồi từ server",
                            Toast.LENGTH_SHORT
                        ).show()

                        txtLastCheckIn.text = "Lỗi phản hồi từ server"
                    }
                }

                override fun onFailure(
                    call: Call<SimpleResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@CheckInActivity,
                        "Lỗi kết nối: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    txtLastCheckIn.text = "Lỗi kết nối server:\n${t.message}"
                }
            })
    }
}