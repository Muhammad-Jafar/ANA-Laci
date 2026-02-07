package proj.yopro.laci.navigation.analyze

import androidx.fragment.app.Fragment

/**
 * Got called when fragment visibility changed.
 */
interface OnHiddenChangedListener {
    fun onHidden(hidden: Boolean)
}

open class MyBaseFragment : Fragment() {
    private val visibilityChangedListeners = ArrayList<OnHiddenChangedListener>()

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        visibilityChangedListeners.forEach { it.onHidden(hidden) }
    }

    override fun onDestroy() {
        super.onDestroy()
        visibilityChangedListeners.clear()
    }

    fun addOnHiddenChangedListener(l: OnHiddenChangedListener) {
        visibilityChangedListeners.add(l)
    }

    fun removeOnHiddenChangedListener(l: OnHiddenChangedListener) {
        visibilityChangedListeners.remove(l)
    }
}
