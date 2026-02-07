package proj.yopro.laci.repositories.repository

import kotlinx.coroutines.flow.Flow
import proj.yopro.laci.local.preference.StoreData
import proj.yopro.laci.repositories.contracts.SettingRepository

class SettingRepoImpl(
    private val store: StoreData,
) : SettingRepository {
    override fun getTheme(): Flow<Int> = store.getTheme()

    override fun getLang(): Flow<String> = store.getLanguage()

    override suspend fun saveTheme(theme: Int) {
        store.saveTheme(theme)
    }

    override suspend fun saveLang(lang: String) {
        store.saveLanguage(lang)
    }
}
