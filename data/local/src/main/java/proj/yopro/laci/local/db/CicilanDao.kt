package proj.yopro.laci.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import proj.yopro.laci.entities.Item
import proj.yopro.laci.entities.ItemLog

@Dao
interface CicilanDao {
    @Query("SELECT COUNT (id) FROM item WHERE status = :status")
    fun count(status: String): Int?

    @Query("SELECT * FROM item WHERE status = :status ORDER BY created_at DESC")
    fun getList(status: String): List<Item>

    @Query("SELECT * FROM item WHERE id = :id")
    fun getById(id: Int): Item

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun store(item: Item)

    @Query("SELECT * FROM ItemLog WHERE cicilan_id = :id ORDER BY date DESC")
    fun getListLog(id: Int): List<ItemLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeLog(itemLog: ItemLog)

    @Query("SELECT nominal_bayar FROM item WHERE id = :id")
    fun getCurrentNominalBayar(id: Int): Int

    @Query("SELECT nominal_lunas FROM item WHERE id = :id")
    fun getCurrentNominalLunas(id: Int): Int

    @Query("UPDATE item SET nominal_lunas = nominal_lunas + :nominal WHERE id = :id")
    suspend fun setNominalLunas(
        id: Int,
        nominal: Int,
    )

    @Query("UPDATE item SET status = :status WHERE id = :id")
    suspend fun setStatusLunas(
        id: Int,
        status: String,
    )

    @Query("UPDATE item SET done_at = :lunasPada WHERE id = :id")
    suspend fun setDateLunas(
        id: Int,
        lunasPada: Long,
    )

    @Query("SELECT image FROM item WHERE id = :id")
    suspend fun getImagePathById(id: Int): String?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Item)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM ItemLog WHERE id = :id")
    suspend fun deleteLog(id: Int)
}
