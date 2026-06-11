package com.example.fitbody.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.model.CartItem
import com.example.fitbody.ui.adapter.CartAdapter
import com.example.fitbody.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var txtTitle: TextView
    private lateinit var recyclerCart: RecyclerView
    private lateinit var txtTotalPrice: TextView

    private val userId = 1

    private val cartList = ArrayList<CartItem>()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btnBack = findViewById(R.id.btnBack)
        txtTitle = findViewById(R.id.txtTitle)
        recyclerCart = findViewById(R.id.recyclerCart)
        txtTotalPrice = findViewById(R.id.txtTotalPrice)

        txtTitle.text = "Giỏ hàng"

        adapter = CartAdapter(cartList)

        recyclerCart.layoutManager =
            LinearLayoutManager(this)

        recyclerCart.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        loadCart()
    }

    private fun loadCart() {
        val session = SessionManager(this)
        val currentUserId = session.getUserId()

        val dbHelper = DatabaseHelper(this)
        val data = dbHelper.getCart(currentUserId)

        cartList.clear()
        cartList.addAll(data)
        adapter.notifyDataSetChanged()
        calculateTotal()

        if (data.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateTotal() {
        val total =
            cartList.sumOf {
                it.price * it.quantity
            }

        val formatter =
            NumberFormat.getInstance(Locale("vi", "VN"))

        txtTotalPrice.text =
            "Tổng tiền: ${formatter.format(total)}đ"
    }
}