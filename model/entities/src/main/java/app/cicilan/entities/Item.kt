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
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long? = null,
    @ColumnInfo(name = "done_at")
    val doneAt: Long? = null,
    @ColumnInfo(name = "image")
    val image: String? = null,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "thing_name")
    val thingName: String = "",
    @ColumnInfo(name = "category")
    val category: String = "",
    @ColumnInfo(name = "price")
    val price: Int = 0,
    @ColumnInfo(name = "uang_muka")
    val uangMuka: Int = 0,
    @ColumnInfo(name = "nominal_bayar")
    val nominalBayar: Int = 0, // harga_barang - uang_muka
    @ColumnInfo(name = "nominal_lunas")
    val nominalLunas: Int = 0, // nominal_per_bulan - nominal_bayar
    @ColumnInfo(name = "period")
    val period: Int = 0,
    @ColumnInfo(name = "tenggat_bayar")
    val tenggatBayar: Int = 0,
    @ColumnInfo(name = "per_bulan")
    val perBulan: Int = 0, // nominal_bayar : periode (Pure utang tanpa laba)
    @ColumnInfo(name = "laba_per_bulan")
    val labaPerBulan: Int = 0, // nominal_bayar * 5%
    @ColumnInfo(name = "nominal_per_bulan")
    val nominalPerBulan: Int = 0, // per bulan + laba
    @ColumnInfo(name = "total_laba")
    val totalLaba: Int = 0,
    @ColumnInfo(name = "status")
    val status: String = "NO",
)
