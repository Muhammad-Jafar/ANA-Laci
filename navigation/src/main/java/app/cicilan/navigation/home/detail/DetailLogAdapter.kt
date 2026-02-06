package app.cicilan.navigation.home.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.cicilan.component.utils.format
import app.cicilan.component.utils.toRupiah
import app.cicilan.entities.ItemLog
import app.cicilan.navigation.databinding.ItemDetailLogBinding

class DetailLogAdapter : ListAdapter<ItemLog, DetailLogAdapter.ViewHolder>(DIFF_CALLBACK_LOG) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder = ViewHolder(ItemDetailLogBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    inner class ViewHolder(
        private val binding: ItemDetailLogBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemLog) =
            with(binding) {
                tanggalTransaksi.text = item.date.format("d.MM.YY HH:mm")
                nominalTransaksi.text = item.amount.toRupiah()
                noteTransaksi.text = item.description.ifBlank { "-" }
            }
    }

    companion object {
        val DIFF_CALLBACK_LOG =
            object : DiffUtil.ItemCallback<ItemLog>() {
                override fun areItemsTheSame(
                    oldItem: ItemLog,
                    newItem: ItemLog,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: ItemLog,
                    newItem: ItemLog,
                ): Boolean = oldItem == newItem
            }
    }
}
