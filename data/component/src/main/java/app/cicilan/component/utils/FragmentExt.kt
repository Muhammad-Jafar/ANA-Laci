package app.cicilan.component.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.R.attr
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by: Muhammad Jafar
 * At: 2/7/26
 * Reach me: 131.powerfull@gmail.com
 */

fun Fragment.runWhenStarted(action: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) { action() }
    }

fun Fragment.runWhenResumed(action: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) { action() }
    }

fun Fragment.showMessage(message: String?) =
    view?.apply {
        Snackbar
            .make(this, message ?: "Unknown message", Snackbar.LENGTH_SHORT)
            .setAnimationMode(ANIMATION_MODE_SLIDE)
            .setBackgroundTint(MaterialColors.getColor(this, attr.colorSurfaceContainer))
            .setTextColor(MaterialColors.getColor(this, attr.colorOnSurface))
            .show()
    }

fun Fragment.popupDialog(
    title: String,
    message: String,
): MaterialAlertDialogBuilder =
    MaterialAlertDialogBuilder(
        requireContext(),
        1,
        // R.style.CustomDialog,
    ).setTitle(title)
        .setMessage(message)
        .setNegativeButton(
            "Cancel",
            // getString(R.string.cancel_button),
        ) { dialog, _ -> dialog.dismiss() }
