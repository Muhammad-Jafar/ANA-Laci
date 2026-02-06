package app.cicilan.navigation

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updatePadding
import app.cicilan.component.utils.runWhenCreated
import app.cicilan.navigation.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SettingsViewModel by viewModel()
    private val gravity: Int = Gravity.BOTTOM
    private val isSystemBarUsed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            runWhenCreated {
                val mode =
                    when (viewModel.getTheme.first()) {
                        1 -> MODE_NIGHT_NO
                        2 -> MODE_NIGHT_YES
                        else -> MODE_NIGHT_FOLLOW_SYSTEM
                    }
                setDefaultNightMode(mode)

                launch {
                    val value = viewModel.getLanguage.first()
                    setApplicationLocales(LocaleListCompat.forLanguageTags(value))
                }
            }
            false
        }

        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWindowInsets()
    }

    private fun initWindowInsets() =
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val imeVisible = windowInsets.isVisible(Type.ime())
            val imeHeight = windowInsets.getInsets(Type.ime()).bottom
            val insetsSystemBar = windowInsets.getInsets(Type.systemBars())

            if (isSystemBarUsed) {
                when (gravity) {
                    Gravity.BOTTOM -> {
                        if (imeVisible) {
                            view.updatePadding(bottom = insetsSystemBar.bottom + imeHeight)
                        } else {
                            view.updatePadding(bottom = insetsSystemBar.bottom)
                        }
                    }
                }
            } else {
                when (gravity) {
                    Gravity.BOTTOM -> {
                        if (imeVisible) {
                            view.updatePadding(bottom = insetsSystemBar.bottom + imeHeight)
                        } else {
                            view.updatePadding(bottom = insetsSystemBar.bottom)
                        }
                    }
                }
            }
            windowInsets
        }
}
