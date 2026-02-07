package proj.yopro.laci.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(
    private val store: DataStore<Preferences>,
) {
    suspend fun saveLanguage(newValue: String) = store.edit { it[lang] = newValue }

    fun getLanguage(): Flow<String> = store.data.map { it[lang] ?: Constanta.DEFAULT_LANGUAGE }

    suspend fun saveTheme(newValue: Int) = store.edit { it[theme] = newValue }

    fun getTheme(): Flow<Int> = store.data.map { it[theme] ?: Constanta.DEFAULT_THEME }

    companion object {
        private val theme = intPreferencesKey(Constanta.StoreData.Theme.name)
        private val lang = stringPreferencesKey(Constanta.StoreData.Language.name)
    }
}
