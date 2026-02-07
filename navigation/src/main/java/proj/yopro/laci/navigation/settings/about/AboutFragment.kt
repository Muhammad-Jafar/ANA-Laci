package proj.yopro.laci.navigation.settings.about

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import proj.yopro.laci.navigation.BaseFragment
import proj.yopro.laci.navigation.databinding.MainSettingsAboutBinding

class AboutFragment : BaseFragment<MainSettingsAboutBinding>(MainSettingsAboutBinding::inflate) {
    override fun renderView(bundle: Bundle?) {
        with(binding) {
            toolbarAbout.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }
}
