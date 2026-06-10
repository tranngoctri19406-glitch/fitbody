package com.example.fitbody.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.MainActivity
import com.example.fitbody.R

class OnboardingActivity : AppCompatActivity() {

    private lateinit var txtSkip: TextView
    private lateinit var progressOnboarding: ProgressBar
    private lateinit var txtQuestion: TextView
    private lateinit var txtSubQuestion: TextView
    private lateinit var layoutOptions: LinearLayout
    private lateinit var btnNext: Button

    private var step = 1

    private var gender = ""
    private val goals = ArrayList<String>()
    private var focusArea = ""
    private var height = ""
    private var weight = ""

    private var weightValue = 65
    private var heightValue = 170

    private var selectedView: TextView? = null
    private var selectedLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        txtSkip = findViewById(R.id.txtSkip)
        progressOnboarding = findViewById(R.id.progressOnboarding)
        txtQuestion = findViewById(R.id.txtQuestion)
        txtSubQuestion = findViewById(R.id.txtSubQuestion)
        layoutOptions = findViewById(R.id.layoutOptions)
        btnNext = findViewById(R.id.btnNext)

        txtSkip.setOnClickListener {
            finishOnboarding()
        }

        btnNext.setOnClickListener {
            handleNext()
        }

        showStep()
    }

    private fun showStep() {
        layoutOptions.removeAllViews()
        layoutOptions.orientation = LinearLayout.VERTICAL
        layoutOptions.gravity = Gravity.CENTER
        layoutOptions.setPadding(0, 0, 0, 0)

        progressOnboarding.progress = step
        btnNext.text = "TIẾP THEO"

        selectedView = null
        selectedLayout = null

        when (step) {
            1 -> showGenderStep()
            2 -> showGoalStep()
            3 -> showFocusStep()
            4 -> showBodyInfoStep()
            5 -> showPlanReadyStep()
        }
    }

    private fun handleNext() {
        when (step) {
            1 -> {
                if (gender.isEmpty()) {
                    showMessage("Vui lòng chọn giới tính")
                } else {
                    step++
                    showStep()
                }
            }

            2 -> {
                if (goals.isEmpty()) {
                    showMessage("Vui lòng chọn ít nhất một mục tiêu")
                } else {
                    step++
                    showStep()
                }
            }

            3 -> {
                if (focusArea.isEmpty()) {
                    showMessage("Vui lòng chọn vùng tập")
                } else {
                    step++
                    showStep()
                }
            }

            4 -> {
                weight = weightValue.toString()
                height = heightValue.toString()
                step++
                showStep()
            }

            5 -> {
                finishOnboarding()
            }
        }
    }

    private fun showGenderStep() {
        txtQuestion.text = "Giới tính của bạn là gì?"
        txtSubQuestion.text = "Hãy cho chúng tôi biết thêm về bạn"

        layoutOptions.orientation = LinearLayout.HORIZONTAL
        layoutOptions.gravity = Gravity.CENTER
        layoutOptions.setPadding(0, 0, 0, 10)

        addGenderOption("Nam", R.drawable.male) {
            gender = "Nam"
        }

        addGenderOption("Nữ", R.drawable.female) {
            gender = "Nữ"
        }
    }

    private fun showGoalStep() {
        txtQuestion.text = "Mục tiêu chính của bạn là gì?"
        txtSubQuestion.text = "Bạn có thể chọn nhiều mục tiêu"

        addMultiOption(
            "🔥 Giảm cân",
            "Đốt mỡ và giảm trọng lượng cơ thể",
            "Giảm cân"
        )

        addMultiOption(
            "💪 Xây dựng cơ bắp",
            "Tăng cơ và vóc dáng săn chắc",
            "Xây dựng cơ bắp"
        )

        addMultiOption(
            "🏃 Giữ dáng",
            "Duy trì vóc dáng hiện tại",
            "Giữ dáng"
        )

        addMultiOption(
            "❤️ Cải thiện sức khỏe",
            "Tăng cường thể lực mỗi ngày",
            "Cải thiện sức khỏe"
        )

        addMultiOption(
            "⚡ Tăng sức bền",
            "Tập lâu hơn và ít mệt hơn",
            "Tăng sức bền"
        )
    }

    private fun showFocusStep() {
        txtQuestion.text = "Hãy chọn vùng tập trung của bạn"
        txtSubQuestion.text = "Bạn muốn tập trung vào phần nào?"

        addOption("🎯  Toàn thân") {
            focusArea = "Toàn thân"
        }

        addOption("💪  Cánh tay") {
            focusArea = "Cánh tay"
        }

        addOption("🏋️  Ngực") {
            focusArea = "Ngực"
        }

        addOption("🔥  Bụng") {
            focusArea = "Bụng"
        }

        addOption("🦵  Chân") {
            focusArea = "Chân"
        }
    }

    private fun showBodyInfoStep() {
        txtQuestion.text = "Hãy cho chúng tôi biết thêm về bạn"
        txtSubQuestion.text = "Chọn cân nặng và chiều cao để tạo kế hoạch"

        layoutOptions.gravity = Gravity.CENTER

        val weightTitle = TextView(this)
        weightTitle.text = "Cân nặng"
        weightTitle.textSize = 28f
        weightTitle.setTextColor(Color.BLACK)
        weightTitle.setTypeface(null, Typeface.BOLD)

        val txtWeightValue = TextView(this)
        txtWeightValue.text = "$weightValue kg"
        txtWeightValue.textSize = 44f
        txtWeightValue.setTextColor(Color.rgb(0, 102, 255))
        txtWeightValue.setTypeface(null, Typeface.BOLD)
        txtWeightValue.gravity = Gravity.CENTER

        val weightControl = createNumberControl(
            onMinus = {
                if (weightValue > 30) {
                    weightValue--
                    txtWeightValue.text = "$weightValue kg"
                }
            },
            onPlus = {
                if (weightValue < 200) {
                    weightValue++
                    txtWeightValue.text = "$weightValue kg"
                }
            }
        )

        val heightTitle = TextView(this)
        heightTitle.text = "Chiều cao"
        heightTitle.textSize = 28f
        heightTitle.setTextColor(Color.BLACK)
        heightTitle.setTypeface(null, Typeface.BOLD)

        val txtHeightValue = TextView(this)
        txtHeightValue.text = "$heightValue cm"
        txtHeightValue.textSize = 44f
        txtHeightValue.setTextColor(Color.rgb(0, 102, 255))
        txtHeightValue.setTypeface(null, Typeface.BOLD)
        txtHeightValue.gravity = Gravity.CENTER

        val heightControl = createNumberControl(
            onMinus = {
                if (heightValue > 120) {
                    heightValue--
                    txtHeightValue.text = "$heightValue cm"
                }
            },
            onPlus = {
                if (heightValue < 220) {
                    heightValue++
                    txtHeightValue.text = "$heightValue cm"
                }
            }
        )

        layoutOptions.addView(weightTitle, createLabelParams())
        layoutOptions.addView(txtWeightValue)
        layoutOptions.addView(weightControl)

        layoutOptions.addView(heightTitle, createLabelParams())
        layoutOptions.addView(txtHeightValue)
        layoutOptions.addView(heightControl)

        btnNext.text = "BẮT ĐẦU KẾ HOẠCH CỦA TÔI"
    }

    private fun createNumberControl(
        onMinus: () -> Unit,
        onPlus: () -> Unit
    ): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(0, 8, 0, 18)

        val btnMinus = Button(this)
        btnMinus.text = "-"
        btnMinus.textSize = 24f

        val btnPlus = Button(this)
        btnPlus.text = "+"
        btnPlus.textSize = 24f

        btnMinus.setOnClickListener {
            onMinus()
        }

        btnPlus.setOnClickListener {
            onPlus()
        }

        val params = LinearLayout.LayoutParams(
            90,
            60
        )
        params.setMargins(16, 0, 16, 0)

        layout.addView(btnMinus, params)
        layout.addView(btnPlus, params)

        return layout
    }

    private fun showPlanReadyStep() {
        txtQuestion.text = "Kế hoạch của bạn đã sẵn sàng!"
        txtSubQuestion.text = "Chúng tôi đã chọn kế hoạch phù hợp với bạn nhất"

        val plan = TextView(this)

        plan.text =
            "TẬP $focusArea\n\n" +
                    "Mục tiêu: ${goals.joinToString(", ")}\n" +
                    "Giới tính: $gender\n" +
                    "Cân nặng: ${weight}kg\n" +
                    "Chiều cao: ${height}cm\n" +
                    "Cấp độ: Cơ bản\n" +
                    "Thiết bị: Không đồ tập"

        plan.textSize = 20f
        plan.setTextColor(Color.WHITE)
        plan.setTypeface(null, Typeface.BOLD)
        plan.setBackgroundColor(Color.rgb(0, 102, 255))
        plan.setPadding(36, 36, 36, 36)

        layoutOptions.addView(
            plan,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        btnNext.text = "BẮT ĐẦU NGAY"
    }

    private fun addGenderOption(
        text: String,
        imageRes: Int,
        onClick: () -> Unit
    ) {
        val itemLayout = LinearLayout(this)
        itemLayout.orientation = LinearLayout.VERTICAL
        itemLayout.gravity = Gravity.CENTER
        itemLayout.setPadding(6, 6, 6, 8)
        itemLayout.setBackgroundColor(Color.TRANSPARENT)
        itemLayout.isClickable = true
        itemLayout.isFocusable = true

        val imageView = ImageView(this)
        imageView.setImageResource(imageRes)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.adjustViewBounds = true

        val imageParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            1200
        )
        val textView = TextView(this)

        textView.text = text
        textView.textSize = 32f
        textView.setTextColor(Color.BLACK)
        textView.setTypeface(null, Typeface.BOLD)
        textView.gravity = Gravity.CENTER
        textView.setPadding(0, 10, 0, 0)

        itemLayout.addView(imageView, imageParams)
        itemLayout.addView(textView)

        itemLayout.setOnClickListener {
            selectedLayout?.setBackgroundColor(Color.TRANSPARENT)

            itemLayout.setBackgroundColor(Color.rgb(230, 240, 255))
            textView.setTextColor(Color.rgb(0, 102, 255))

            selectedLayout = itemLayout
            onClick()
        }

        val params = LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.weight = 1f
        params.setMargins(4, 0, 4, 0)

        layoutOptions.addView(itemLayout, params)
    }

    private fun addOption(
        text: String,
        onClick: () -> Unit
    ) {
        val option = TextView(this)
        option.text = text
        option.textSize = 20f
        option.setTextColor(Color.BLACK)
        option.setTypeface(null, Typeface.BOLD)
        option.gravity = Gravity.CENTER_VERTICAL
        option.setPadding(34, 0, 34, 0)
        option.setBackgroundColor(Color.rgb(248, 248, 248))

        option.setOnClickListener {
            selectedView?.setBackgroundColor(Color.rgb(248, 248, 248))
            selectedView?.setTextColor(Color.BLACK)

            option.setBackgroundColor(Color.rgb(0, 102, 255))
            option.setTextColor(Color.WHITE)

            selectedView = option
            onClick()
        }

        layoutOptions.addView(option, createOptionParams())
    }

    private fun addMultiOption(
        title: String,
        description: String,
        value: String
    ) {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30, 16, 30, 16)
        layout.setBackgroundColor(Color.rgb(248, 248, 248))

        val titleView = TextView(this)
        titleView.text = title
        titleView.textSize = 18f
        titleView.setTextColor(Color.BLACK)
        titleView.setTypeface(null, Typeface.BOLD)

        val descView = TextView(this)
        descView.text = description
        descView.textSize = 13f
        descView.setTextColor(Color.GRAY)
        descView.setPadding(0, 6, 0, 0)

        layout.addView(titleView)
        layout.addView(descView)

        layout.setOnClickListener {
            if (goals.contains(value)) {
                goals.remove(value)

                layout.setBackgroundColor(Color.rgb(248, 248, 248))
                titleView.setTextColor(Color.BLACK)
                descView.setTextColor(Color.GRAY)
            } else {
                goals.add(value)

                layout.setBackgroundColor(Color.rgb(0, 102, 255))
                titleView.setTextColor(Color.WHITE)
                descView.setTextColor(Color.WHITE)
            }
        }

        layoutOptions.addView(layout, createGoalOptionParams())
    }

    private fun createOptionParams(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            86
        )
        params.setMargins(0, 12, 0, 12)
        return params
    }

    private fun createGoalOptionParams(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            105
        )
        params.setMargins(0, 10, 0, 10)
        return params
    }

    private fun createLabelParams(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 18, 0, 8)
        return params
    }

    private fun finishOnboarding() {
        val userId =
            intent.getIntExtra("user_id", 0)

        val sharedPreferences =
            getSharedPreferences(
                "onboarding_data",
                Context.MODE_PRIVATE
            )

        val completedKey =
            "is_onboarding_completed_$userId"

        sharedPreferences.edit()
            .putBoolean(completedKey, true)
            .putString("gender_$userId", gender)
            .putString("goal_$userId", goals.joinToString(", "))
            .putString("focus_area_$userId", focusArea)
            .putString("height_$userId", height)
            .putString("weight_$userId", weight)
            .apply()

        val intent =
            Intent(
                this,
                MainActivity::class.java
            )

        intent.putExtra("user_id", userId)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}