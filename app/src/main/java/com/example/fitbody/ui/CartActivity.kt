package com.example.fitbody.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.CartItem
import com.example.fitbody.ui.adapter.CartAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        RetrofitClient.instance.getCart(userId)
            .enqueue(object : Callback<List<CartItem>> {

                override fun onResponse(
                    call: Call<List<CartItem>>,
                    response: Response<List<CartItem>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        cartList.clear()
                        cartList.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                        calculateTotal()
                    } else {
                        Toast.makeText(
                            this@CartActivity,
                            "Không lấy được giỏ hàng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<CartItem>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@CartActivity,
                        "Lỗi kết nối: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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