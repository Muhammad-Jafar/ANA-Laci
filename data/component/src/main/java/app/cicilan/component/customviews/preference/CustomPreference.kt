package app.cicilan.component.customviews.preference

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import app.cicilan.component.R
import com.google.android.material.imageview.ShapeableImageView

class CustomPreference
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0,
    ) : Preference(context, attrs, defStyleAttr) {
        private val typedArray =
            context
                .obtainStyledAttributes(attrs, R.styleable.CustomPreference, 0, 0)

        private var mValue = typedArray.getString(R.styleable.CustomPreference_valuePreference)

        init {
            layoutResource = R.layout.custom_preference
        }

        override fun onBindViewHolder(holder: PreferenceViewHolder) {
            super.onBindViewHolder(holder)
            with(holder) {
                isDividerAllowedAbove = false
                val icon = findViewById(R.id.iconPreference) as ShapeableImageView
                val title = findViewById(R.id.titlePreference) as TextView
                val value = findViewById(R.id.valuePreference) as TextView

                typedArray.apply {
                    icon.setImageResource(getResourceId(R.styleable.CustomPreference_iconPreference, 0))
                    title.text = getString(R.styleable.CustomPreference_titlePreference)
                    value.text = gettingValue()
                }
            }
        }

        private fun gettingValue() = mValue

        fun setValue(value: String?) {
            if (!TextUtils.equals(mValue, value)) mValue = value
            notifyChanged()
        }
    }
