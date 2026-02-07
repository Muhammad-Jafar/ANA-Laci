package proj.yopro.laci.repositories.usecases

import kotlinx.coroutines.flow.Flow
import proj.yopro.laci.repositories.contracts.CicilanRepository

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class CountCicilanUseCase(
    private val repo: CicilanRepository,
) {
    operator fun invoke(status: String): Flow<Int> = repo.count(status)
}
