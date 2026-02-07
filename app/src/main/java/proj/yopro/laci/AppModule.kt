package proj.yopro.laci

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.android.material.color.DynamicColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import proj.yopro.laci.local.db.provideDao
import proj.yopro.laci.local.db.provideDatabase
import proj.yopro.laci.local.preference.StoreData
import proj.yopro.laci.navigation.DetailViewModel
import proj.yopro.laci.navigation.FormViewModel
import proj.yopro.laci.navigation.HomeViewModel
import proj.yopro.laci.navigation.MainActivity
import proj.yopro.laci.navigation.SettingsViewModel
import proj.yopro.laci.navigation.home.HomeFragment
import proj.yopro.laci.navigation.home.MainListFragment
import proj.yopro.laci.navigation.home.detail.DetailFragment
import proj.yopro.laci.navigation.home.detail.DetailLogFragment
import proj.yopro.laci.navigation.settings.about.AboutFragment
import proj.yopro.laci.navigation.settings.donate.DonateFragment
import proj.yopro.laci.repositories.contracts.CicilanLogRepository
import proj.yopro.laci.repositories.contracts.CicilanRepository
import proj.yopro.laci.repositories.contracts.SettingRepository
import proj.yopro.laci.repositories.repository.CicilanLogRepoImpl
import proj.yopro.laci.repositories.repository.CicilanRepoImpl
import proj.yopro.laci.repositories.repository.SettingRepoImpl
import proj.yopro.laci.repositories.usecases.CountCicilanUseCase
import proj.yopro.laci.repositories.usecases.DeleteCicilanUseCase
import proj.yopro.laci.repositories.usecases.GetCicilanByIdUseCase
import proj.yopro.laci.repositories.usecases.GetListCicilanLogUseCase
import proj.yopro.laci.repositories.usecases.GetListCicilanUseCase
import proj.yopro.laci.repositories.usecases.InsertCicilanLogUseCase
import proj.yopro.laci.repositories.usecases.InsertCicilanUseCase

/**
 * Created by: Muhammad Jafar
 * At: 06 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class AppModule : Application() {
    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        val fragmentViewModule =
            module {
                scope<proj.yopro.laci.navigation.MainActivity> {
                    fragment {
                        _root_ide_package_.proj.yopro.laci.navigation.home
                            .HomeFragment()
                    }
                    fragment {
                        _root_ide_package_.proj.yopro.laci.navigation.home
                            .MainListFragment()
                    }
                    fragment {
                        _root_ide_package_.proj.yopro.laci.navigation.home.detail
                            .DetailFragment()
                    }
                    fragment {
                        _root_ide_package_.proj.yopro.laci.navigation.home.detail
                            .DetailLogFragment()
                    }
                    fragment {
                        _root_ide_package_.proj.yopro.laci.navigation.settings.about
                            .AboutFragment()
                    }
                    fragment {
                        _root_ide_package_.proj.yopro.laci.navigation.settings.donate
                            .DonateFragment()
                    }
                }
            }

        val dispatcherKoinModule =
            module {
                single { Dispatchers.IO }
                single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
            }

        val databaseModule =
            module {
                single { provideDatabase(androidContext()) }
                single { provideDao(get()) }
            }

        val dataStoreModule =
            module {
                single {
                    PreferenceDataStoreFactory.create {
                        androidContext().preferencesDataStoreFile("CicilanDataStore")
                    }
                }
                single { StoreData(get()) }
            }

        val repositoryModule =
            module {
                singleOf(::CicilanRepoImpl) { bind<CicilanRepository>() }
                singleOf(::CicilanLogRepoImpl) { bind<CicilanLogRepository>() }
                singleOf(::SettingRepoImpl) { bind<SettingRepository>() }
            }

        val usecaseModule =
            module {
                singleOf(::GetListCicilanUseCase)
                singleOf(::GetListCicilanLogUseCase)
                singleOf(::GetCicilanByIdUseCase)
                singleOf(::CountCicilanUseCase)
                singleOf(::InsertCicilanUseCase)
                singleOf(::InsertCicilanLogUseCase)
                singleOf(::DeleteCicilanUseCase)
            }

        val viewModelModule =
            module {
                viewModelOf(::HomeViewModel)
                viewModelOf(::DetailViewModel)
                viewModelOf(::FormViewModel)
                viewModelOf(::SettingsViewModel)
            }

        startKoin {
            androidLogger()
            androidContext(this@AppModule)
            fragmentFactory()
            modules(
                listOf(
                    dispatcherKoinModule,
                    fragmentViewModule,
                    databaseModule,
                    dataStoreModule,
                    repositoryModule,
                    usecaseModule,
                    viewModelModule,
                ),
            )
        }
    }
}
