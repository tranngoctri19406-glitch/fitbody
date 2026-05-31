package com.example.fitbody

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "Màn hình chat sẽ làm sau"
        textView.textSize = 22f
        textView.setPadding(40, 80, 40, 40)

        setContentView(textView)
    }
}