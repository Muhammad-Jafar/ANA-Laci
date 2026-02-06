package app.cicilan.component.customviews.button

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import app.cicilan.component.R
import com.google.android.material.radiobutton.MaterialRadioButton

class CustomCheckbox : ConstraintLayout {
    private lateinit var checkbox: MaterialRadioButton
    private lateinit var typedArray: TypedArray

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    private fun initView(
        context: Context,
        attrs: AttributeSet,
    ) {
        inflate(context, R.layout.custom_checkbox, this)

        checkbox = findViewById(R.id.customCheckBox)
        val title = findViewById<TextView>(R.id.titleCheckbox)
        val subtitle = findViewById<TextView>(R.id.subtitleCheckbox)

        context.obtainStyledAttributes(attrs, R.styleable.CustomCheckbox, 0, 0).apply {
            try {
                title.text = getString(R.styleable.CustomCheckbox_titleState)
                subtitle.text = getString(R.styleable.CustomCheckbox_subtitleState)
            } finally {
                recycle()
            }
        }
    }

    fun setState(state: Boolean) {
        this.checkbox.isChecked = state
    }
}
