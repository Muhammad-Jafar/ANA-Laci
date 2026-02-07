package proj.yopro.laci.repositories.contracts

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun getTheme(): Flow<Int>

    fun getLang(): Flow<String>

    suspend fun saveTheme(theme: Int)

    suspend fun saveLang(lang: String)
}
