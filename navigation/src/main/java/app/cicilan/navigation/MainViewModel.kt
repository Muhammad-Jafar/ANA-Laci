package app.cicilan.navigation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.cicilan.component.utils.mapWithStateInWhileSubscribed
import app.cicilan.component.utils.runInBackground
import app.cicilan.entities.CalculateState
import app.cicilan.entities.Item
import app.cicilan.entities.ItemLog
import app.cicilan.entities.ModalForm
import app.cicilan.repositories.contracts.SettingRepository
import app.cicilan.repositories.usecases.CountCicilanUseCase
import app.cicilan.repositories.usecases.DeleteCicilanUseCase
import app.cicilan.repositories.usecases.GetCicilanByIdUseCase
import app.cicilan.repositories.usecases.GetListCicilanLogUseCase
import app.cicilan.repositories.usecases.GetListCicilanUseCase
import app.cicilan.repositories.usecases.InsertCicilanLogUseCase
import app.cicilan.repositories.usecases.InsertCicilanUseCase
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

open class MainViewModel : ViewModel()

class HomeViewModel(
    countCicilan: CountCicilanUseCase,
    private val listCicilan: GetListCicilanUseCase,
) : MainViewModel() {
    val getTotalCurrent =
        countCicilan("NO")
            .mapWithStateInWhileSubscribed(0)
    val getTotalDone =
        countCicilan("YES")
            .mapWithStateInWhileSubscribed(0)
    private val _perBulanValue = MutableLiveData(CalculateState())
    val perBulanValue get() = _perBulanValue
    var labaValue = 0

    fun getList(status: String) =
        listCicilan(status)
            .mapWithStateInWhileSubscribed(listOf())

    fun calculate(
        harga: Int,
        dp: Int,
        periode: Int,
    ) {
        when {
            harga < 1 -> {
                _perBulanValue.value = CalculateState(hargaError = R.string.fill_data)
            }

            dp < 1 -> {
                _perBulanValue.value = CalculateState(dpError = R.string.fill_data)
            }

            dp > harga -> {
                _perBulanValue.value = CalculateState(dpError = R.string.form_dp_lessThan_harga)
            }

            periode < 1 -> {
                _perBulanValue.value = CalculateState(periodeError = R.string.fill_data)
            }

            else -> {
                val nominalMembayar = harga - dp
                val nominalPerBulan = (nominalMembayar / periode)
                val laba = (nominalMembayar * 0.05).toInt()
                _perBulanValue.value =
                    CalculateState(isResultThere = nominalPerBulan)
                labaValue = laba
            }
        }
    }
}

class FormViewModel(
    private val cicilan: InsertCicilanUseCase,
) : MainViewModel() {
    private val _dataModal = MutableSharedFlow<ModalForm>()
    val dataModal get() = _dataModal
    var imageUri: Uri? = null

    fun save(item: ModalForm) = runInBackground { cicilan.invoke(item) }
}

class DetailViewModel(
    private val getById: GetCicilanByIdUseCase,
    private val getLog: GetListCicilanLogUseCase,
    private val storeLog: InsertCicilanLogUseCase,
    private val delete: DeleteCicilanUseCase,
) : MainViewModel() {
    fun getCicilanById(id: Int) =
        getById(id)
            .mapWithStateInWhileSubscribed(Item())

    fun getCicilanLog(id: Int) =
        getLog(id)
            .mapWithStateInWhileSubscribed(listOf(ItemLog()))

    fun storeCicilanLog(item: ItemLog) = runInBackground { storeLog(item) }

    fun deleteCicilan(id: Int) = runInBackground { delete(id) }
}

class SettingsViewModel(
    private val repo: SettingRepository,
) : MainViewModel() {
    val getTheme = repo.getTheme()
    val getLanguage = repo.getLang()

    fun saveThemeValue(value: Int) = runInBackground { repo.saveTheme(value) }

    fun saveLangValue(value: String) = runInBackground { repo.saveLang(value) }
}
