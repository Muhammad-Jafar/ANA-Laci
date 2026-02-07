package proj.yopro.laci.navigation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import proj.yopro.laci.component.utils.afterInputNumberChanged
import proj.yopro.laci.component.utils.digitNumberArranged
import proj.yopro.laci.component.utils.getNumber
import proj.yopro.laci.component.utils.runWhenResumed
import proj.yopro.laci.component.utils.runWhenStarted
import proj.yopro.laci.component.utils.toRupiah
import proj.yopro.laci.navigation.BaseFragment
import proj.yopro.laci.navigation.HomeViewModel
import proj.yopro.laci.navigation.R
import proj.yopro.laci.navigation.databinding.DialogCalculateCicilanBinding
import proj.yopro.laci.navigation.databinding.MainHomeBinding

class HomeFragment : BaseFragment<MainHomeBinding>(MainHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModel()

    override fun renderView(bundle: Bundle?) {
        with(binding) {
            with(viewModel) {
                runWhenStarted {
                    launch { getTotalCurrent.collectLatest { belumLunasContent.text = it.toString() } }
                    launch { getTotalDone.collectLatest { sudahLunasContent.text = it.toString() } }
                }
            }
            toolbarHome.apply {
                inflateMenu(R.menu.main_option_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.settingMenu -> findNavController().navigate(R.id.action_main_to_settings)
                        R.id.prediksiCicilan -> showCalculate()
                    }
                    true
                }
            }
            addItem.setOnClickListener { findNavController().navigate(R.id.action_main_to_form) }

            viewOfTabs.adapter = SectionPagerAdapter()
            viewOfTabs.registerOnPageChangeCallback(
                object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (position == 0) {
                            addItem.show()
                        } else {
                            addItem.hide()
                        }
                        super.onPageSelected(position)
                    }
                },
            )

            val tabTitle = listOf(R.string.debt_now, R.string.debt_done)
            TabLayoutMediator(tabs, viewOfTabs) { tab, position ->
                tab.text = getString(tabTitle[position])
            }.attach()
        }
    }

    private fun showCalculate() {
        val dialogForm = DialogCalculateCicilanBinding.inflate(layoutInflater)
        val dialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialog).apply {
                setCancelable(false)
                setTitle("Mini calculator cicilan")
                setView(dialogForm.root)
                setPositiveButton(getString(R.string.ok_button)) { dialog, _ -> dialog.dismiss() }
            }
        with(dialogForm) {
            calculateLabel.text = 0.toRupiah()
            labaLabel.text = 0.toRupiah()
            hargaInput.digitNumberArranged(hargaInputLayout)
            dpInput.digitNumberArranged(dpInputLayout)
            periodeInput.digitNumberArranged(periodeInputLayout)

            with(viewModel) {
                runWhenResumed {
                    perBulanValue.observe(viewLifecycleOwner) { state ->
                        calculateLabel.text =
                            when {
                                state.hargaError != null -> getString(R.string.calculating)
                                state.dpError != null -> getString(R.string.calculating)
                                state.periodeError != null -> getString(R.string.calculating)
                                else -> state.isResultThere.toRupiah()
                            }
                        labaLabel.setTextColor(getColor(labaLabel, R.attr.colorLeaf))
                        labaLabel.text = "+ ".plus(labaValue.toRupiah()).plus("/ Bulan")
                    }
                }

                hargaInput.afterInputNumberChanged {
                    calculate(
                        hargaInput.text.getNumber(),
                        dpInput.text.getNumber(),
                        periodeInput.text.getNumber(),
                    )
                }
                dpInput.afterInputNumberChanged {
                    calculate(
                        hargaInput.text.getNumber(),
                        dpInput.text.getNumber(),
                        periodeInput.text.getNumber(),
                    )
                }
                periodeInput.afterInputNumberChanged {
                    calculate(
                        hargaInput.text.getNumber(),
                        dpInput.text.getNumber(),
                        periodeInput.text.getNumber(),
                    )
                }
            }
        }
        dialog.show()
    }

    inner class SectionPagerAdapter : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            val fragment = MainListFragment()
            val data = Bundle()

            if (position == 0) {
                data.putString(ARGS_TAB, TAB_CURRENT)
            } else {
                data.putString(ARGS_TAB, TAB_DONE)
            }

            fragment.arguments = data
            return fragment
        }
    }

    companion object {
        // Home list
        const val ARGS_TAB = "Tab argument"
        const val TAB_CURRENT = "Tab current"
        const val TAB_DONE = "Tab done"
    }
}
