package proj.yopro.laci.navigation.home.done

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import proj.yopro.laci.entities.Item
import proj.yopro.laci.navigation.R
import proj.yopro.laci.navigation.databinding.ItemDoneBinding
import proj.yopro.laci.navigation.home.HomeFragmentDirections
import proj.yopro.laci.navigation.home.current.CurrentAdapter.Companion.DIFF_CALLBACK

class DoneAdapter : ListAdapter<Item, DoneAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder = ViewHolder(ItemDoneBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    class ViewHolder(
        private val binding: ItemDoneBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Item) =
            with(binding) {
                avatar.apply {
                    scaleType =
                        if (items.image != null) {
                            setImageURI(items.image!!.toUri())
                            ImageView.ScaleType.CENTER_CROP
                        } else {
                            setBackgroundColor(
                                MaterialColors.getColor(
                                    this.rootView,
                                    R.attr.colorCustomContainer,
                                ),
                            )
                            setImageResource(R.drawable.icon_bg_image)
                            ImageView.ScaleType.CENTER_INSIDE
                        }
                }

                nameProduct.text = items.thingName
                nameUser.text = items.name
            /*totalLaba.text = "+ ".plus(items.totalLabaToRupiah)
            lunasPada.text = items.lunasPadaFormat*/

                itemView.setOnClickListener {
                    val direction =
                        HomeFragmentDirections.actionMainToDetail(items.id!!)
                    it.findNavController().navigate(direction)
                }
            }
    }

    companion object {
        val DIFF_CALLBACK_LOG =
            object : DiffUtil.ItemCallback<Item>() {
                override fun areItemsTheSame(
                    oldItem: Item,
                    newItem: Item,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: Item,
                    newItem: Item,
                ): Boolean = oldItem == newItem
            }
    }
}
