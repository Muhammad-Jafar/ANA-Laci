package proj.yopro.laci.component.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import proj.yopro.laci.component.R

/**
 * Created by: Muhammad Jafar
 * At: 2/7/26
 * Reach me: 131.powerfull@gmail.com
 */

enum class KeyboardState {
    HIDE,
    SHOW,
}

fun TextInputEditText.setSoftKeyboard(state: KeyboardState) {
    val inputMethod =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return

    when (state) {
        KeyboardState.HIDE -> inputMethod.hideSoftInputFromWindow(windowToken, 0)
        else -> inputMethod.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun TextInputEditText.afterInputNumberChanged(afterTextChanged: (Int?) -> Unit) =
    afterTextChanged { editable ->
        afterTextChanged(editable?.getNumber())
    }

fun TextInputEditText.afterInputStringChanged(afterTextChanged: (String?) -> Unit) =
    afterTextChanged { editable ->
        afterTextChanged(editable?.toString())
    }

fun MaterialAutoCompleteTextView.afterInputStringChanged(afterTextChanged: (String?) -> Unit) =
    afterTextChanged { editable ->
        afterTextChanged(editable?.toString())
    }

private fun TextView.afterTextChanged(onAfterTextChanged: (Editable?) -> Unit) {
    this.addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) = Unit

            override fun afterTextChanged(s: Editable?) {
                onAfterTextChanged(s)
            }
        },
    )
}

fun Editable?.getNumber(): Int =
    this
        ?.filter { it.isDigit() }
        ?.toString()
        ?.toIntOrNull()
        ?: 0

fun TextInputEditText.digitNumberArranged(layout: TextInputLayout) {
    addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(
                textChanged: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) = Unit

            override fun afterTextChanged(editable: Editable?) {
                val digits =
                    editable
                        ?.filter(Char::isDigit)
                        ?.toString()
                        .orEmpty()
                val digitValue = digits.toIntOrNull()
                val formatted = digitValue.toRupiah()

                if (digits.isEmpty()) {
                    text?.clear()
                    layout.error = null
                    return
                }

                if (digitValue == null) {
                    layout.error = context.getString(R.string.input_over)
                    return
                }

                setText(formatted)
                setSelection(formatted.length)
                layout.error = null
            }
        },
    )
}
