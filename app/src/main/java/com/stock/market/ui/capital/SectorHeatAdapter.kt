package com.stock.market.ui.capital

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stock.market.R
import com.stock.market.databinding.ItemSectorHeatBinding
import com.stock.market.model.Sector

/**
 * 板块热力图适配器
 * 根据涨跌幅显示红涨绿跌，颜色深浅按涨幅大小
 */
class SectorHeatAdapter : RecyclerView.Adapter<SectorHeatAdapter.ViewHolder>() {

    private var sectors: List<Sector> = emptyList()

    fun submitList(list: List<Sector>) {
        sectors = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSectorHeatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sectors[position])
    }

    override fun getItemCount(): Int = sectors.size

    class ViewHolder(private val binding: ItemSectorHeatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sector: Sector) {
            binding.tvSectorName.text = sector.name

            val changeText = if (sector.isUp) {
                "+${String.format("%.2f", sector.changePercent)}%"
            } else {
                "${String.format("%.2f", sector.changePercent)}%"
            }
            binding.tvChangePercent.text = changeText

            // 根据涨跌幅设置背景颜色（红涨绿跌，深浅按幅度）
            val bgColor = getHeatColor(sector.changePercent, sector.isUp)
            binding.layoutHeatItem.setBackgroundColor(
                ContextCompat.getColor(binding.root.context, bgColor)
            )

            // 浅色背景用深色文字，深色背景用白色文字
            val textColor = if (Math.abs(sector.changePercent) < 1.0) {
                R.color.text_primary
            } else {
                R.color.white
            }
            binding.tvSectorName.setTextColor(
                ContextCompat.getColor(binding.root.context, textColor)
            )
            binding.tvChangePercent.setTextColor(
                ContextCompat.getColor(binding.root.context, textColor)
            )
        }

        /**
         * 根据涨跌幅返回对应的热力图颜色
         */
        private fun getHeatColor(change: Double, isUp: Boolean): Int {
            val absChange = Math.abs(change)
            return if (isUp) {
                when {
                    absChange >= 5.0 -> R.color.heat_up_deep
                    absChange >= 2.0 -> R.color.heat_up_medium
                    absChange >= 1.0 -> R.color.heat_up_light
                    else -> R.color.heat_up_very_light
                }
            } else {
                when {
                    absChange >= 5.0 -> R.color.heat_down_deep
                    absChange >= 2.0 -> R.color.heat_down_medium
                    absChange >= 1.0 -> R.color.heat_down_light
                    else -> R.color.heat_down_very_light
                }
            }
        }
    }
}
