package proj.yopro.laci.repositories.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import proj.yopro.laci.entities.ItemLog
import proj.yopro.laci.local.db.CicilanDao
import proj.yopro.laci.repositories.contracts.CicilanLogRepository

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class CicilanLogRepoImpl(
    private val dao: CicilanDao,
) : CicilanLogRepository {
    override fun getLog(id: Int): Flow<List<ItemLog>> =
        flow {
            val getLog = dao.getListLog(id)

            emit(getLog)
        }
}
