package com.stock.market.ui.capital

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stock.market.R
import com.stock.market.databinding.ItemSectorCapitalFlowBinding
import com.stock.market.model.SectorCapitalFlow

/**
 * 板块资金流入列表适配器
 */
class SectorCapitalFlowAdapter : ListAdapter<SectorCapitalFlow, SectorCapitalFlowAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSectorCapitalFlowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemSectorCapitalFlowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SectorCapitalFlow) {
            // 排名
            binding.tvRank.text = item.rank.toString()
            val rankBg = when (item.rank) {
                1 -> R.drawable.bg_rank_first
                2 -> R.drawable.bg_rank_second
                3 -> R.drawable.bg_rank_third
                else -> R.drawable.bg_rank_normal
            }
            binding.tvRank.setBackgroundResource(rankBg)

            // 板块名称和代码
            binding.tvSectorName.text = item.name
            binding.tvSectorCode.text = item.code

            // 主力净流入
            val inflowText = if (item.netInflow >= 0) {
                "+${String.format("%.2f", item.netInflow)}亿"
            } else {
                "${String.format("%.2f", item.netInflow)}亿"
            }
            binding.tvNetInflow.text = inflowText
            binding.tvNetInflow.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (item.netInflow >= 0) R.color.stock_up else R.color.stock_down
                )
            )

            // 当日涨跌幅
            val changeText = if (item.isUp) {
                "+${String.format("%.2f", item.changePercent)}%"
            } else {
                "${String.format("%.2f", item.changePercent)}%"
            }
            binding.tvChangePercent.text = changeText
            val colorRes = if (item.isUp) R.color.stock_up else R.color.stock_down
            binding.tvChangePercent.setTextColor(
                ContextCompat.getColor(binding.root.context, colorRes)
            )
            binding.tvChangePercent.setBackgroundResource(
                if (item.isUp) R.drawable.bg_change_up else R.drawable.bg_change_down
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SectorCapitalFlow>() {
        override fun areItemsTheSame(oldItem: SectorCapitalFlow, newItem: SectorCapitalFlow): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: SectorCapitalFlow, newItem: SectorCapitalFlow): Boolean {
            return oldItem == newItem
        }
    }
}
