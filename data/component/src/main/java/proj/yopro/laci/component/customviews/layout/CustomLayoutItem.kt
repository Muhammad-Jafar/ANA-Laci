package proj.yopro.laci.component.customviews.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.R.attr
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDivider
import proj.yopro.laci.component.R

class CustomLayoutItem : ConstraintLayout {
    private lateinit var titleItem: TextView
    private lateinit var contentItem: TextView

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
        inflate(context, R.layout.custom_layout_item, this)

        titleItem = findViewById(R.id.titleLayoutItem)
        contentItem = findViewById(R.id.contentLayoutItem)
        val divider = findViewById<MaterialDivider>(R.id.dividerLayoutItem)

        context.obtainStyledAttributes(attrs, R.styleable.CustomLayoutItem, 0, 0).apply {
            try {
                titleItem.text = getString(R.styleable.CustomLayoutItem_titleItem)
                contentItem.text = getString(R.styleable.CustomLayoutItem_contentItem)
                divider.apply {
                    dividerColor = MaterialColors.getColor(rootView, attr.colorSurfaceVariant)
                    visibility = getInt(R.styleable.CustomLayoutItem_hideDividerItem, 0)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setTitleItem(titleItem: String) {
        this.titleItem.text = titleItem
    }

    fun setContentItem(contentItem: String) {
        this.contentItem.text = contentItem
    }
}
