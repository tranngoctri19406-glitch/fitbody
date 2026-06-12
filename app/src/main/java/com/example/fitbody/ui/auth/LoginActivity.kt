package com.example.fitbody.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.MainActivity
import com.example.fitbody.PtMainActivity
import com.example.fitbody.R
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.ui.OnboardingActivity
import com.example.fitbody.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var btnGoogle: ImageButton
    private lateinit var btnFacebook: ImageButton

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        
        // Áp dụng chế độ sáng/tối khi mở app
        if (session.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (session.isLoggedIn()) {
            openMainOrOnboarding()
            return
        }

        setContentView(R.layout.activity_login)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnFacebook = findViewById(R.id.btnFacebook)

        // Cấu hình Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Cấu hình Facebook Login
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // Xử lý khi đăng nhập Facebook thành công
                handleSocialLogin(result.accessToken.userId, "Facebook", "User Facebook", "")
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Hủy đăng nhập Facebook", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(this@LoginActivity, "Lỗi Facebook: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        btnLogin.setOnClickListener {
            login()
        }

        txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        btnFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    handleSocialLogin(account.id ?: "", "Google", account.displayName ?: "User", account.email ?: "")
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Lỗi Google Sign In: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSocialLogin(socialId: String, provider: String, name: String, email: String) {
        val dbHelper = DatabaseHelper(this)
        var userId = dbHelper.getUserBySocialId(socialId, provider)

        if (userId == -1) {
            // Đăng ký mới nếu chưa có
            userId = dbHelper.registerSocialUser(name, email, socialId, provider).toInt()
        }

        if (userId != -1) {
            val session = SessionManager(this)
            session.saveLogin(name, "user", userId)
            openMainOrOnboarding(userId, name)
        } else {
            Toast.makeText(this, "Lỗi đăng nhập $provider", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login() {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val userId = dbHelper.checkUser(username, password)

        if (userId != -1) {
            val session = SessionManager(this)
            session.saveLogin(username, "user", userId)
            
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
            openMainOrOnboarding(userId, username)
        } else {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMainOrOnboarding(
        userId: Int = 0,
        username: String = ""
    ) {
        val session = SessionManager(this)
        val actualUserId = if (userId == 0) session.getUserId() else userId
        val actualUsername = if (username.isEmpty()) session.getUsername() else username

        val sharedPreferences =
            getSharedPreferences(
                "onboarding_data",
                Context.MODE_PRIVATE
            )

        val key =
            "is_onboarding_completed_$actualUserId"

        val isCompleted =
            sharedPreferences.getBoolean(
                key,
                false
            )

        val intent =
            if (isCompleted) {
                Intent(
                    this,
                    MainActivity::class.java
                )
            } else {
                Intent(
                    this,
                    OnboardingActivity::class.java
                )
            }

        intent.putExtra("user_id", actualUserId)
        intent.putExtra("username", actualUsername)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun openPt(userId: Int, username: String) {
        val intent =
            Intent(
                this,
                PtMainActivity::class.java
            )

        intent.putExtra("user_id", userId)
        intent.putExtra("username", username)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}