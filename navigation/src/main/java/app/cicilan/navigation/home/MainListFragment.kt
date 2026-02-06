package app.cicilan.navigation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import app.cicilan.component.utils.runWhenResumed
import app.cicilan.navigation.HomeViewModel
import app.cicilan.navigation.databinding.MainHomeListBinding
import app.cicilan.navigation.home.HomeFragment.Companion.ARGS_TAB
import app.cicilan.navigation.home.HomeFragment.Companion.TAB_CURRENT
import app.cicilan.navigation.home.HomeFragment.Companion.TAB_DONE
import app.cicilan.navigation.home.current.CurrentAdapter
import app.cicilan.navigation.home.done.DoneAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainListFragment : Fragment() {
    private var _binding: MainHomeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()
    private val currentAdapter = CurrentAdapter().apply { stateRestorationPolicy = PREVENT_WHEN_EMPTY }
    private val doneAdapter = DoneAdapter().apply { stateRestorationPolicy = PREVENT_WHEN_EMPTY }
    private var tabName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            MainHomeListBinding.inflate(
                LayoutInflater.from(parentFragment?.context),
                container,
                false,
            )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        tabName = arguments?.getString(ARGS_TAB)

        with(binding) {
            if (tabName == TAB_CURRENT) {
                rvCicilanBerjalan.adapter = currentAdapter
                collectData("NO", TAB_CURRENT)
            } else {
                rvCicilanLunas.adapter = doneAdapter
                collectData("YES", TAB_DONE)
            }
        }
    }

    private fun collectData(
        status: String,
        tabPosition: String,
    ) {
        runWhenResumed {
            val recyclerView =
                if (tabPosition == TAB_CURRENT) {
                    binding.rvCicilanBerjalan
                } else {
                    binding.rvCicilanLunas
                }
            val adapter =
                if (tabPosition == TAB_CURRENT) {
                    currentAdapter
                } else {
                    doneAdapter
                }

            viewModel
                .getList(status)
                .collect { list ->
                    binding.progressBar.visibility = View.GONE

                    if (list.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        binding.viewEmptyCurrent.visibility = View.VISIBLE
                    } else {
                        binding.viewEmptyCurrent.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter.submitList(list)
                    }
                }
        }
    }
}
