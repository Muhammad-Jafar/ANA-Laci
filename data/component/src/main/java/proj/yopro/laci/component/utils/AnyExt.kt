package proj.yopro.laci.component.utils

import android.Manifest
import android.content.res.Resources
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.datetime.TimeZone
import java.text.NumberFormat
import java.time.Instant.ofEpochMilli
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val currentInstant: Long = Clock.System.now().toEpochMilliseconds()

fun Int?.toRupiah(): String =
    NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        .apply { maximumFractionDigits = 0 }
        .format(this)
        ?: "Rp. 0"

@OptIn(ExperimentalTime::class)
fun Long?.format(
    pattern: String,
    locale: Locale = Locale.getDefault(),
    timezone: TimeZone = TimeZone.currentSystemDefault(),
): String =
    DateTimeFormatter
        .ofPattern(pattern)
        .withZone(ZoneId.of(timezone.id))
        .format(ofEpochMilli(this ?: Clock.System.now().toEpochMilliseconds()))

fun Int.dotPixel() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

@RequiresPermission(Manifest.permission.VIBRATE)
fun View.vibrate() =
    getSystemService(context, Vibrator::class.java)
        ?.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
