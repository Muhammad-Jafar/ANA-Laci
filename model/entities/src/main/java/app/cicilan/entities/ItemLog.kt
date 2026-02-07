package app.cicilan.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

@Entity
data class ItemLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "cicilan_id")
    val cicilanId: Int? = null,
    @ColumnInfo(name = "date")
    val date: Long? = null,
    @ColumnInfo(name = "amount")
    val amount: Int = 0,
    @ColumnInfo(name = "description")
    val description: String = "",
)
