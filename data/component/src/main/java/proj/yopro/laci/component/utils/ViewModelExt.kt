package proj.yopro.laci.component.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Created by: Muhammad Jafar
 * At: 2/7/26
 * Reach me: 131.powerfull@gmail.com
 */

context(model: ViewModel)
fun runInBackground(action: suspend CoroutineScope.() -> Unit) = model.viewModelScope.launch(Dispatchers.IO) { action() }

context(model: ViewModel)
fun <T> Flow<T>.mapWithStateInWhileSubscribed(initialValue: T): StateFlow<T> =
    this
        .map { it }
        .stateIn(
            scope = model.viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialValue,
        )
