package app.cicilan.component.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by: Muhammad Jafar
 * At: 2/7/26
 * Reach me: 131.powerfull@gmail.com
 */

fun AppCompatActivity.runWhenCreated(action: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.CREATED) { action() } }
