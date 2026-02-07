package proj.yopro.laci.navigation.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.net.toUri
import androidx.core.os.LocaleListCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel
import proj.yopro.laci.component.customviews.preference.CustomPreference
import proj.yopro.laci.component.utils.dotPixel
import proj.yopro.laci.component.utils.runWhenResumed
import proj.yopro.laci.component.utils.runWhenStarted
import proj.yopro.laci.component.utils.showMessage
import proj.yopro.laci.navigation.BaseFragment
import proj.yopro.laci.navigation.R
import proj.yopro.laci.navigation.SettingsViewModel
import proj.yopro.laci.navigation.databinding.DialogSettingsLanguageBinding
import proj.yopro.laci.navigation.databinding.DialogSettingsThemeBinding
import proj.yopro.laci.navigation.databinding.MainSettingsBinding

class SettingsFragment : BaseFragment<MainSettingsBinding>(MainSettingsBinding::inflate) {
    override fun renderView(bundle: Bundle?) {
        binding.toolbarSettings.setNavigationOnClickListener { findNavController().navigateUp() }
        if (bundle == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.settingMenuContainer, MenuSettingsFragment())
                .commit()
        }
    }
}

class MenuSettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?,
    ) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<CustomPreference>(getString(R.string.key_theme))
            ?.apply {
                runWhenStarted {
                    val theme =
                        when (viewModel.getTheme.first()) {
                            1 -> getString(R.string.light_theme)
                            2 -> getString(R.string.dark_theme)
                            else -> getString(R.string.default_mode)
                        }
                    setValue(theme)
                }
                setOnPreferenceClickListener {
                    val dialogTheme = DialogSettingsThemeBinding.inflate(layoutInflater)
                    val formTheme =
                        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog)
                            .apply {
                                setContentView(dialogTheme.root)
                                behavior.maxHeight = 600.dotPixel()
                                dismissWithAnimation = true
                            }
                    with(dialogTheme) {
                        root.doOnPreDraw { formTheme.behavior.peekHeight = it.height }
                        runWhenResumed {
                            with(viewModel) {
                                when (getTheme.first()) {
                                    1 -> lightMode.setState(true)
                                    2 -> darkMode.setState(true)
                                    else -> defaultMode.setState(true)
                                }
                            }
                        }

                        defaultMode.apply {
                            isClickable = true
                            setOnClickListener {
                                viewModel.saveThemeValue(-1)
                                setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                                setState(true)
                                lightMode.setState(false)
                                darkMode.setState(false)
                            }
                        }

                        lightMode.apply {
                            isClickable = true
                            setOnClickListener {
                                viewModel.saveThemeValue(1)
                                setDefaultNightMode(MODE_NIGHT_NO)
                                setState(true)
                                darkMode.setState(false)
                                defaultMode.setState(false)
                            }
                        }

                        darkMode.apply {
                            isClickable = true
                            setOnClickListener {
                                viewModel.saveThemeValue(2)
                                setDefaultNightMode(MODE_NIGHT_YES)
                                setState(true)
                                defaultMode.setState(false)
                                lightMode.setState(false)
                            }
                        }
                    }
                    formTheme.show()
                    true
                }
            }

        findPreference<CustomPreference>(getString(R.string.key_language))?.apply {
            runWhenStarted {
                val language =
                    when (viewModel.getLanguage.first()) {
                        "in" -> getString(R.string.default_language)
                        else -> getString(R.string.english_internasional)
                    }
                setValue(language)
            }

            fun setLanguage(lang: String?) = AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))

            setOnPreferenceClickListener {
                val dialogLang = DialogSettingsLanguageBinding.inflate(layoutInflater)
                val formLang =
                    BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog).apply {
                        setContentView(dialogLang.root)
                        behavior.maxHeight = 600.dotPixel()
                        dismissWithAnimation = true
                    }
                with(dialogLang) {
                    root.doOnPreDraw { formLang.behavior.peekHeight = it.height }
                    runWhenResumed {
                        with(viewModel) {
                            when (getLanguage.first()) {
                                "in" -> indoLang.setState(true)
                                else -> engLang.setState(true)
                            }
                        }
                    }

                    indoLang.apply {
                        isClickable = true
                        setOnClickListener {
                            viewModel.saveLangValue("in")
                            setLanguage("in")
                            engLang.setState(false)
                            setState(true)
                        }
                    }

                    engLang.apply {
                        isClickable = true
                        setOnClickListener {
                            viewModel.saveLangValue("en-US")
                            setLanguage("en-US")
                            indoLang.setState(false)
                            setState(true)
                        }
                    }
                }
                formLang.show()
                true
            }
        }

        findPreference<Preference>(getString(R.string.key_feedback))
            ?.setOnPreferenceClickListener {
                val email = EMAIL
                val subject = getString(R.string.title_share_feedback)
                startActivity(
                    Intent(Intent.ACTION_SENDTO).setData("mailto:$email?subject=$subject".toUri()),
                )
                true
            }

        findPreference<Preference>(getString(R.string.key_rating))
            ?.setOnPreferenceClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData("https://play.google.com/store/apps/details?id=app.cicilan".toUri())
                        .setPackage("com.android.vending"),
                )
                true
            }

        findPreference<Preference>(getString(R.string.key_donate))
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_settings_to_donate)
                true
            }

        findPreference<Preference>(getString(R.string.key_privacy))
            ?.setOnPreferenceClickListener {
                showMessage(getString(R.string.otw_desc))
                true
            }

        findPreference<Preference>(getString(R.string.key_faq))
            ?.setOnPreferenceClickListener {
                showMessage(getString(R.string.otw_desc))
                true
            }

        findPreference<CustomPreference>(getString(R.string.key_about))
            ?.apply {
                // setValue("v".plus(BuildConfig.VERSION_NAME))
                setOnPreferenceClickListener {
                    findNavController().navigate(R.id.action_settings_to_about)
                    true
                }
            }
    }

    companion object {
        const val EMAIL = "131.powerfull@gmail.com"
    }
}
