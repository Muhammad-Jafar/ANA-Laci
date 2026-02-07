package proj.yopro.laci.navigation.settings.donate

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import proj.yopro.laci.navigation.BaseFragment
import proj.yopro.laci.navigation.databinding.MainSettingsDonateBinding

class DonateFragment : BaseFragment<MainSettingsDonateBinding>(MainSettingsDonateBinding::inflate) {
    override fun renderView(bundle: Bundle?) {
        with(binding) {
            toolbarDonate.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }
}
