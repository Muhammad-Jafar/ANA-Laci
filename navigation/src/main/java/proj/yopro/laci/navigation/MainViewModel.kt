package proj.yopro.laci.navigation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import proj.yopro.laci.component.utils.mapWithStateInWhileSubscribed
import proj.yopro.laci.component.utils.runInBackground
import proj.yopro.laci.entities.CalculateState
import proj.yopro.laci.entities.Item
import proj.yopro.laci.entities.ItemLog
import proj.yopro.laci.entities.ModalForm
import proj.yopro.laci.repositories.contracts.SettingRepository
import proj.yopro.laci.repositories.usecases.CountCicilanUseCase
import proj.yopro.laci.repositories.usecases.DeleteCicilanUseCase
import proj.yopro.laci.repositories.usecases.GetCicilanByIdUseCase
import proj.yopro.laci.repositories.usecases.GetListCicilanLogUseCase
import proj.yopro.laci.repositories.usecases.GetListCicilanUseCase
import proj.yopro.laci.repositories.usecases.InsertCicilanLogUseCase
import proj.yopro.laci.repositories.usecases.InsertCicilanUseCase

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

open class MainViewModel : ViewModel()

class HomeViewModel(
    countCicilan: CountCicilanUseCase,
    private val listCicilan: GetListCicilanUseCase,
) : proj.yopro.laci.navigation.MainViewModel() {
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
) : proj.yopro.laci.navigation.MainViewModel() {
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
) : proj.yopro.laci.navigation.MainViewModel() {
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
) : proj.yopro.laci.navigation.MainViewModel() {
    val getTheme = repo.getTheme()
    val getLanguage = repo.getLang()

    fun saveThemeValue(value: Int) = runInBackground { repo.saveTheme(value) }

    fun saveLangValue(value: String) = runInBackground { repo.saveLang(value) }
}
