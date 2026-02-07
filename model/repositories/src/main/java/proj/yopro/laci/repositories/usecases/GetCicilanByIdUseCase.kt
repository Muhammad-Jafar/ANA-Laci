package proj.yopro.laci.repositories.usecases

import proj.yopro.laci.repositories.contracts.CicilanRepository

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class GetCicilanByIdUseCase(
    private val repo: CicilanRepository,
) {
    operator fun invoke(id: Int) = repo.getById(id)
}
