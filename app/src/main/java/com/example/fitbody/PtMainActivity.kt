package com.example.fitbody

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitbody.database.DatabaseHelper

class PtMainActivity : AppCompatActivity() {

    private var ptId: Int = 0
    private var ptName: String = ""
    private lateinit var btnBack: TextView
    private lateinit var imgProfilePt: ImageView
    private lateinit var dbHelper: DatabaseHelper

    // Bộ chọn ảnh từ thư viện
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                // Lưu URI vào SQLite
                updateImageInSqlite(imageUri.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_main)

        dbHelper = DatabaseHelper(this)
        
        // Nhận ID từ Intent (giả sử ID là 1 nếu không truyền)
        ptId = intent.getIntExtra("user_id", 1)
        ptName = intent.getStringExtra("username") ?: "PT"

        btnBack = findViewById(R.id.btnBack)
        imgProfilePt = findViewById(R.id.imgProfilePt)
        val txtWelcome = findViewById<TextView>(R.id.txtWelcomePt)
        val btnAddWorkout = findViewById<Button>(R.id.btnOpenAddWorkout)
        val btnChangeImage = findViewById<Button>(R.id.btnChangeImage)

        txtWelcome.text = "Xin chào PT: $ptName"

        // Load ảnh hiện tại từ SQLite
        loadCurrentProfileImage()

        btnBack.setOnClickListener {
            finish()
        }

        btnAddWorkout.setOnClickListener {
            val intent = Intent(this, AddWorkoutActivity::class.java)
            intent.putExtra("trainer_id", ptId)
            startActivity(intent)
        }

        btnChangeImage.setOnClickListener {
            // Mở thư viện chọn ảnh
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }
    }

    private fun loadCurrentProfileImage() {
        val trainers = dbHelper.getAllTrainers()
        val currentPt = trainers.find { it.id == ptId }
        
        if (currentPt != null && currentPt.image.isNotEmpty()) {
            val imageResId = resources.getIdentifier(currentPt.image, "drawable", packageName)
            if (imageResId != 0) {
                Glide.with(this)
                    .load(imageResId)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgProfilePt)
            } else {
                Glide.with(this)
                    .load(currentPt.image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgProfilePt)
            }
        }
    }

    private fun updateImageInSqlite(imagePath: String) {
        if (dbHelper.updateTrainerImage(ptId, imagePath)) {
            Toast.makeText(this, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show()
            // Hiển thị lại ảnh mới
            Glide.with(this).load(imagePath).into(imgProfilePt)
        } else {
            Toast.makeText(this, "Lỗi cập nhật SQLite", Toast.LENGTH_SHORT).show()
        }
    }
}