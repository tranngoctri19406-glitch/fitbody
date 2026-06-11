package com.example.fitbody.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R
import com.example.fitbody.utils.SessionManager

class BMICalculatorActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var txtTitle: TextView
    private lateinit var edtHeight: EditText
    private lateinit var edtWeight: EditText
    private lateinit var btnCalculateBMI: Button
    private lateinit var txtBMIResult: TextView
    private lateinit var txtBMIStatus: TextView

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_calculator)

        btnBack = findViewById(R.id.btnBack)
        txtTitle = findViewById(R.id.txtTitle)
        edtHeight = findViewById(R.id.edtHeight)
        edtWeight = findViewById(R.id.edtWeight)
        btnCalculateBMI = findViewById(R.id.btnCalculateBMI)
        txtBMIResult = findViewById(R.id.txtBMIResult)
        txtBMIStatus = findViewById(R.id.txtBMIStatus)

        txtTitle.text = "BMI Calculator"

        val session = SessionManager(this)
        userId = session.getUserId()

        btnBack.setOnClickListener {
            finish()
        }

        btnCalculateBMI.setOnClickListener {
            calculateBMI(saveData = true)
        }

        loadSavedDataAndCalculate()
    }

    private fun loadSavedDataAndCalculate() {
        val sharedPreferences =
            getSharedPreferences("onboarding_data", Context.MODE_PRIVATE)

        val savedWeight =
            sharedPreferences.getString("weight_$userId", "")

        val savedHeight =
            sharedPreferences.getString("height_$userId", "")

        if (!savedWeight.isNullOrEmpty()) {
            edtWeight.setText(savedWeight)
        }

        if (!savedHeight.isNullOrEmpty()) {
            edtHeight.setText(savedHeight)
        }

        if (!savedWeight.isNullOrEmpty() && !savedHeight.isNullOrEmpty()) {
            calculateBMI(saveData = false)
        }
    }

    private fun calculateBMI(saveData: Boolean) {
        val heightText = edtHeight.text.toString().trim()
        val weightText = edtWeight.text.toString().trim()

        if (heightText.isEmpty() || weightText.isEmpty()) {
            Toast.makeText(
                this,
                "Vui lòng nhập chiều cao và cân nặng",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            val heightCm = heightText.toDouble()
            val weightKg = weightText.toDouble()

            if (heightCm <= 0 || weightKg <= 0) {
                Toast.makeText(
                    this,
                    "Chiều cao và cân nặng phải lớn hơn 0",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            val heightM = heightCm / 100
            val bmi = weightKg / (heightM * heightM)
            val bmiText = String.format("%.2f", bmi)

            txtBMIResult.text = "BMI của bạn: $bmiText"
            txtBMIStatus.text = getBMIStatus(bmi)

            if (saveData) {
                saveHeightAndWeight(heightText, weightText)

                Toast.makeText(
                    this,
                    "Đã cập nhật chiều cao và cân nặng",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Vui lòng nhập số hợp lệ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveHeightAndWeight(
        height: String,
        weight: String
    ) {
        val sharedPreferences =
            getSharedPreferences("onboarding_data", Context.MODE_PRIVATE)

        sharedPreferences.edit()
            .putString("height_$userId", height)
            .putString("weight_$userId", weight)
            .apply()
    }

    private fun getBMIStatus(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Trạng thái: Gầy"
            bmi < 25 -> "Trạng thái: Bình thường"
            bmi < 30 -> "Trạng thái: Thừa cân"
            else -> "Trạng thái: Béo phì"
        }
    }
}