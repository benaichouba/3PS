package com.example.englishadventure

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var root: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
        showStartScreen()
    }

    /**
     * The supplied artwork is the actual screen artwork, not a decorative image.
     * FIT_XY makes the artwork occupy the complete phone display so overlay controls
     * stay aligned with the fields/buttons drawn in the artwork.
     */
    private fun baseScreen(imageRes: Int): FrameLayout {
        root = FrameLayout(this)
        root.setBackgroundColor(Color.WHITE)

        val background = ImageView(this).apply {
            setImageResource(imageRes)
            scaleType = ImageView.ScaleType.FIT_XY
            isClickable = false
        }
        root.addView(background, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))
        return root
    }

    private fun showStartScreen() {
        val view = baseScreen(R.drawable.buttons)

        // Exact transparent hit zones over the two illustrated buttons.
        addPercentView(view, .08f, .12f, .84f, .31f) { showSignUpScreen() }
        addPercentView(view, .08f, .57f, .84f, .31f) { finish() }

        setContentView(view)
    }

    private fun showSignUpScreen() {
        val view = baseScreen(R.drawable.sign_up_page)

        // The four EditTexts are positioned over the empty boxes in the supplied image.
        val fields = listOf(
            addField(view, "Full Name", .405f, .356f),
            addField(view, "Age", .405f, .428f),
            addField(view, "School's Name", .405f, .500f),
            addField(view, "Activation Code", .405f, .573f)
        )
        fields[1].inputType = InputType.TYPE_CLASS_NUMBER
        fields[3].inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

        // The green Sign Up button is already drawn in the artwork.
        addPercentView(view, .25f, .748f, .50f, .092f) {
            val name = fields[0].text.toString().trim()
            val age = fields[1].text.toString().trim()
            val school = fields[2].text.toString().trim()
            val code = fields[3].text.toString().trim()

            if (name.isEmpty() || age.isEmpty() || school.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, "Please complete all four fields.", Toast.LENGTH_SHORT).show()
            } else {
                // Activation/payment logic will be added in a later step.
                showMainScreen()
            }
        }

        setContentView(view)
    }

    private fun addField(
        parent: FrameLayout,
        hint: String,
        leftPercent: Float,
        topPercent: Float
    ): EditText {
        val field = EditText(this).apply {
            this.hint = hint
            textSize = 16f
            setTextColor(Color.rgb(25, 55, 95))
            setHintTextColor(Color.rgb(145, 165, 180))
            setSingleLine(true)
            setPadding(0, 0, 0, 0)
            background = ColorDrawable(Color.TRANSPARENT)
            gravity = Gravity.CENTER_VERTICAL
            includeFontPadding = false
        }
        val params = FrameLayout.LayoutParams(0, 0)
        parent.addView(field, params)
        parent.post {
            params.leftMargin = (parent.width * leftPercent).toInt()
            params.topMargin = (parent.height * topPercent).toInt()
            params.width = (parent.width * .285f).toInt()
            params.height = (parent.height * .055f).toInt()
            field.layoutParams = params
        }
        return field
    }

    private fun addPercentView(
        parent: FrameLayout,
        left: Float,
        top: Float,
        width: Float,
        height: Float,
        action: () -> Unit
    ) {
        val hit = View(this).apply {
            setBackgroundColor(Color.TRANSPARENT)
            setOnClickListener { action() }
        }
        val params = FrameLayout.LayoutParams(0, 0)
        parent.addView(hit, params)
        parent.post {
            params.leftMargin = (parent.width * left).toInt()
            params.topMargin = (parent.height * top).toInt()
            params.width = (parent.width * width).toInt()
            params.height = (parent.height * height).toInt()
            hit.layoutParams = params
        }
    }

    private fun showMainScreen() {
        // This is the supplied educational main-page artwork itself.
        // No duplicate text/buttons are drawn over it.
        val view = baseScreen(R.drawable.main_page)

        // Transparent hit areas match the three illustrated arrow buttons.
        // Content for these sections will be added in the next build step.
        addPercentView(view, .76f, .37f, .16f, .10f) {
            Toast.makeText(this, "Pre-Sequence", Toast.LENGTH_SHORT).show()
        }
        addPercentView(view, .76f, .55f, .16f, .10f) {
            Toast.makeText(this, "Main Lessons", Toast.LENGTH_SHORT).show()
        }
        addPercentView(view, .76f, .72f, .16f, .10f) {
            Toast.makeText(this, "Assessment", Toast.LENGTH_SHORT).show()
        }
        setContentView(view)
    }
}
