package proj.yopro.laci.repositories.contracts

import kotlinx.coroutines.flow.Flow
import proj.yopro.laci.entities.ItemLog

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

interface CicilanLogRepository {
    fun getLog(id: Int): Flow<List<ItemLog>>
}
