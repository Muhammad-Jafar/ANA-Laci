package app.cicilan.repositories.repository

import androidx.core.net.toUri
import androidx.room.withTransaction
import app.cicilan.component.util.currentDate
import app.cicilan.entities.Item
import app.cicilan.entities.ItemLog
import app.cicilan.entities.ModalForm
import app.cicilan.local.db.CicilanDao
import app.cicilan.local.db.CicilanDb
import app.cicilan.repositories.contracts.CicilanRepository
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class CicilanRepoImpl(
    private val db: CicilanDb,
    private val dao: CicilanDao,
) : CicilanRepository {

    override fun get(status: String): Flow<List<Item>> =
        flow {
            val list = dao.getList(status)

            emit(list)
        }

    override fun getById(id: Int): Flow<Item> =
        flow {
            val getCicilan = dao.getById(id)

            emit(getCicilan)
        }

    override fun count(status: String): Flow<Int> =
        flow {
            val counted = dao.count(status)

            if (counted != null) emit(counted)
            else emit(0)
        }

    override suspend fun insert(data: ModalForm) {
        val id = data.id
        val nominalMembayar = data.price - data.uangMuka
        val perBulan = nominalMembayar / data.period
        val laba = (nominalMembayar * 0.05).toInt()
        val totalLaba = laba * data.period
        val nominalPerBulan = (perBulan + laba)
        val item = Item(
            id = id,
            createdAt = currentDate,
            doneAt = null,
            image = data.image,
            name = data.person,
            thingName = data.thing,
            price = data.price,
            category = data.category,
            uangMuka = data.uangMuka,
            nominalBayar = nominalMembayar,
            nominalLunas = 0,
            period = data.period,
            tenggatBayar = data.tenggatBayar,
            perBulan = perBulan,
            labaPerBulan = laba,
            nominalPerBulan = nominalPerBulan,
            totalLaba = totalLaba,
            status = "NO",
        )

        dao.store(item)
    }

    override suspend fun update(data: ModalForm) {
        if (data.id != null) {
            val cicilan = dao.getById(data.id!!)

            with(cicilan) {
                val image =
                    if (data.image == image) {
                        image
                    } else {
                        data.image
                    }
                val nominalMembayar = data.price - data.uangMuka
                val perBulan = nominalMembayar / data.period
                val laba = (nominalMembayar * 0.05).toInt()
                val totalLaba = laba * data.period
                val nominalPerBulan = (perBulan + laba)
                val item = Item(
                    id, createdAt, null, image, data.person,
                    data.thing, data.category, data.price, data.uangMuka,
                    nominalMembayar, nominalLunas, data.period, data.tenggatBayar,
                    perBulan, laba, nominalPerBulan, totalLaba, "NO",
                )

                dao.update(item)
            }
        }
    }

    override suspend fun insertLog(data: ItemLog) {
        with(data) {
            val nominalBayar = dao.getCurrentNominalBayar(cicilanId!!)
            val nominalLunas = dao.getCurrentNominalLunas(cicilanId!!)
            val currentNominaLunas = data.amount + nominalLunas

            if (currentNominaLunas == nominalBayar) {
                runBlocking {
                    db.withTransaction {
                        dao.setNominalLunas(cicilanId!!, amount)
                        dao.storeLog(data)
                    }
                    db.withTransaction {
                        dao.setStatusLunas(cicilanId!!, "YES")
                        dao.setDateLunas(cicilanId!!, currentDate)
                    }
                }
            } else {
                db.withTransaction {
                    dao.setNominalLunas(cicilanId!!, amount)
                    dao.storeLog(data)
                }
            }
        }
    }

    override suspend fun delete(id: Int) {
        db.withTransaction {
            with(dao) {
                this.delete(id)
                deleteLog(id)
                getImagePathById(id).also {
                    it?.toUri()
                        ?.path
                        ?.let { path -> File(path).delete() }
                }
            }
        }
    }
}
