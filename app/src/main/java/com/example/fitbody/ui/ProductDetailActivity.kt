package com.example.fitbody.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.SimpleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var txtTitle: TextView
    private lateinit var imgProductDetail: ImageView
    private lateinit var txtProductNameDetail: TextView
    private lateinit var txtProductCategoryDetail: TextView
    private lateinit var txtProductPriceDetail: TextView
    private lateinit var txtProductDescriptionDetail: TextView
    private lateinit var btnAddToCart: Button

    private var productId = 0
    private var productName = ""
    private var productPrice = 0
    private var productImage = ""
    private var productDescription = ""
    private var productCategory = ""

    private val userId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        btnBack = findViewById(R.id.btnBack)
        txtTitle = findViewById(R.id.txtTitle)
        imgProductDetail = findViewById(R.id.imgProductDetail)
        txtProductNameDetail = findViewById(R.id.txtProductNameDetail)
        txtProductCategoryDetail = findViewById(R.id.txtProductCategoryDetail)
        txtProductPriceDetail = findViewById(R.id.txtProductPriceDetail)
        txtProductDescriptionDetail = findViewById(R.id.txtProductDescriptionDetail)
        btnAddToCart = findViewById(R.id.btnAddToCart)

        txtTitle.text = "Chi tiết sản phẩm"

        productId = intent.getIntExtra("product_id", 0)
        productName = intent.getStringExtra("product_name") ?: ""
        productPrice = intent.getIntExtra("product_price", 0)
        productImage = intent.getStringExtra("product_image") ?: ""
        productDescription = intent.getStringExtra("product_description") ?: ""
        productCategory = intent.getStringExtra("product_category") ?: ""

        txtProductNameDetail.text = productName
        txtProductCategoryDetail.text = productCategory
        txtProductDescriptionDetail.text = productDescription

        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        txtProductPriceDetail.text = formatter.format(productPrice) + "đ"

        Glide.with(this)
            .load(productImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgProductDetail)

        btnBack.setOnClickListener {
            finish()
        }

        btnAddToCart.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        if (productId == 0) {
            Toast.makeText(
                this,
                "Sản phẩm không hợp lệ",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        btnAddToCart.isEnabled = false
        btnAddToCart.text = "ĐANG THÊM..."

        RetrofitClient.instance.addToCart(
            userId,
            productId
        ).enqueue(object : Callback<SimpleResponse> {

            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                btnAddToCart.isEnabled = true
                btnAddToCart.text = "THÊM VÀO GIỎ HÀNG"

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    Toast.makeText(
                        this@ProductDetailActivity,
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Lỗi phản hồi từ server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<SimpleResponse>,
                t: Throwable
            ) {
                btnAddToCart.isEnabled = true
                btnAddToCart.text = "THÊM VÀO GIỎ HÀNG"

                Toast.makeText(
                    this@ProductDetailActivity,
                    "Lỗi kết nối: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}