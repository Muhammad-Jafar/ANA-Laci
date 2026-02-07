package proj.yopro.laci.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

@Parcelize
data class ModalForm(
    val id: Int?,
    val image: String? = null,
    val person: String,
    val thing: String,
    val category: String,
    val price: Int,
    val uangMuka: Int,
    val period: Int,
    val tenggatBayar: Int, // FIXME: consider to use long as date instead of int
) : Parcelable
