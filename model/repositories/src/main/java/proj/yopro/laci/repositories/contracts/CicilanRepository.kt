package proj.yopro.laci.repositories.contracts

import kotlinx.coroutines.flow.Flow
import proj.yopro.laci.entities.Item
import proj.yopro.laci.entities.ItemLog
import proj.yopro.laci.entities.ModalForm

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

interface CicilanRepository {
    fun get(status: String): Flow<List<Item>>

    fun getById(id: Int): Flow<Item>

    fun count(status: String): Flow<Int>

    suspend fun insert(data: ModalForm)

    suspend fun insertLog(data: ItemLog)

    suspend fun update(data: ModalForm)

    suspend fun delete(id: Int)
}
