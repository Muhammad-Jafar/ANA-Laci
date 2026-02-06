package app.cicilan.component.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.util.*

/**
 * Created by: Muhammad Jafar
 * At: 2/7/26
 * Reach me: 131.powerfull@gmail.com
 */

fun TextInputEditText.showSoftKeyboard() {
    if (this.requestFocus()) {
        (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun TextInputEditText.hideSoftKeyboard() {
    (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextInputEditText.afterInputNumberChanged(afterTextChanged: (Int) -> Unit) =
    this.addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int,
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.getNumber())
            }
        },
    )

fun TextInputEditText.afterInputStringChanged(afterTextChanged: (String?) -> Unit) =
    this.addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int,
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        },
    )

fun MaterialAutoCompleteTextView.afterInputStringChanged(afterTextChanged: (String?) -> Unit) =
    this.addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int,
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        },
    )

fun Editable?.getNumber(): Int = this?.filter { it.isDigit() }?.toString()?.toIntOrNull() ?: 0

fun TextInputEditText.addAutoConverterToMoneyFormat(layout: TextInputLayout) =
    addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {
                s?.filter { it.isDigit() }?.toString()?.let {
                    removeTextChangedListener(this)
                    if (it.isNotEmpty()) {
                        val result = it.toIntOrNull()
                        if (result != null) {
                            val format =
                                NumberFormat
                                    .getInstance(Locale("in"))
                                    .apply { maximumFractionDigits = 0 }
                                    .format(result)
                            setText(format)
                            setSelection(format.length)
                        } else {
                            layout.error = "Max limit"
                            // context.getString(R.string.input_over)
                        }
                    } else {
                        text?.clear()
                    }

                    addTextChangedListener(this)
                    layout.error = null
                }
            }
        },
    )
