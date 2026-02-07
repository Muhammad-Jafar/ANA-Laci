package proj.yopro.laci.repositories.usecases

import proj.yopro.laci.repositories.contracts.CicilanLogRepository

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class GetListCicilanLogUseCase(
    private val repo: CicilanLogRepository,
) {
    operator fun invoke(idLog: Int) = repo.getLog(idLog)
}
