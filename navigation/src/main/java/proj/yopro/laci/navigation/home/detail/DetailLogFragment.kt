package proj.yopro.laci.navigation.home.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import proj.yopro.laci.component.utils.runWhenResumed
import proj.yopro.laci.navigation.BaseFragment
import proj.yopro.laci.navigation.DetailViewModel
import proj.yopro.laci.navigation.databinding.MainDetailLogBinding

/**
 * Created by: Muhammad Jafar
 * At: 04 Nov 23
 * Find me: 131.powerfull@gmail.com
 */

class DetailLogFragment : BaseFragment<MainDetailLogBinding>(MainDetailLogBinding::inflate) {
    private val viewModel: DetailViewModel by viewModel()
    private val args: DetailLogFragmentArgs by navArgs()
    private val logAdapter = DetailLogAdapter()

    override fun renderView(bundle: Bundle?) {
        with(binding) {
            toolbarDetailLog.setNavigationOnClickListener { findNavController().navigateUp() }
            rvDetailLog.adapter = logAdapter

            runWhenResumed {
                viewModel
                    .getCicilanLog(args.cicilanId)
                    .collect { state ->
                        if (state.isEmpty()) {
                            viewEmpty.visibility = View.VISIBLE
                            rvDetailLog.visibility = View.GONE
                        } else {
                            viewEmpty.visibility = View.GONE
                            rvDetailLog.visibility = View.VISIBLE
                            logAdapter.submitList(state)
                        }
                    }
            }
        }
    }
}
