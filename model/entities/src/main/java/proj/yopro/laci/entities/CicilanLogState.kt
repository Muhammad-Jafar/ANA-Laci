package proj.yopro.laci.entities

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

data class CicilanLogState(
    val isLoading: Boolean = false,
    val log: List<ItemLog> = listOf(),
    val error: String = "",
)
