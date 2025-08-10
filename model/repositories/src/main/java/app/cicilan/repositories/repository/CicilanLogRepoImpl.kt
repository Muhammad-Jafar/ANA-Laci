package app.cicilan.repositories.repository

import app.cicilan.entities.ItemLog
import app.cicilan.local.db.CicilanDao
import app.cicilan.repositories.contracts.CicilanLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
