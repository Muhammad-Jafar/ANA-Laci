package app.cicilan.component.customviews.layout

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import app.cicilan.component.R
import com.google.android.material.R.attr
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.imageview.ShapeableImageView

class CustomLayoutMenu : ConstraintLayout {
    private lateinit var content: TextView
    private lateinit var typedArray: TypedArray

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    private fun initView(
        context: Context,
        attrs: AttributeSet,
    ) {
        inflate(context, R.layout.custom_layout_menu, this)

        val icon = findViewById<ShapeableImageView>(R.id.iconLayoutMenu)
        val title = findViewById<TextView>(R.id.titleLayoutMenu)
        content = findViewById(R.id.contentLayoutMenu)
        val divider = findViewById<MaterialDivider>(R.id.dividerLayoutMenu)

        context.obtainStyledAttributes(attrs, R.styleable.CustomLayoutMenu, 0, 0).apply {
            try {
                icon.setImageResource(getResourceId(R.styleable.CustomLayoutMenu_iconMenu, 0))
                title.text = getString(R.styleable.CustomLayoutMenu_titleMenu)
                content.text = getString(R.styleable.CustomLayoutMenu_contentMenu)
                divider.apply {
                    dividerColor = MaterialColors.getColor(rootView, attr.colorSurfaceVariant)
                    visibility = getInt(R.styleable.CustomLayoutMenu_hideDividerMenu, 0)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setContentLayout(content: String) {
        this.content.text = content
    }

    fun setTextColor(color: ColorStateList) {
        this.content.setTextColor(color)
    }
}
