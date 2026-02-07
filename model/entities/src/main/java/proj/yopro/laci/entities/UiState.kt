package proj.yopro.laci.entities

/**
 * Created by: Inisialku
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()

    data class Error(
        val message: String,
    ) : UiState<Nothing>()

    data class Success<T>(
        val data: T,
    ) : UiState<T>()
}

typealias homeUiState = UiState<Item>
typealias logCicilanUiState = UiState<ItemLog>
