package com.stock.market.ui.capital

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.stock.market.databinding.FragmentCapitalFlowBinding
import com.stock.market.model.Sector
import com.stock.market.network.StockRepository
import java.util.Calendar

/**
 * 资金流入板块 - 上涨/下跌热力图 + 主力资金流入板块排行
 */
class CapitalFlowFragment : Fragment() {

    private var _binding: FragmentCapitalFlowBinding? = null
    private val binding get() = _binding!!
    private lateinit var heatUpAdapter: SectorHeatAdapter
    private lateinit var heatDownAdapter: SectorHeatAdapter
    private lateinit var capitalAdapter: SectorCapitalFlowAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 60000L // 60秒自动刷新

    private val refreshRunnable = object : Runnable {
        override fun run() {
            loadData()
            handler.postDelayed(this, refreshInterval)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCapitalFlowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeatMaps()
        setupCapitalList()
        loadData()

        binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(refreshRunnable, refreshInterval)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    /**
     * 配置上涨/下跌热力图网格
     */
    private fun setupHeatMaps() {
        // 上涨热力图
        heatUpAdapter = SectorHeatAdapter()
        binding.recyclerViewHeatUp.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = heatUpAdapter
            setHasFixedSize(true)
        }

        // 下跌热力图
        heatDownAdapter = SectorHeatAdapter()
        binding.recyclerViewHeatDown.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = heatDownAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * 配置板块资金流列表
     */
    private fun setupCapitalList() {
        capitalAdapter = SectorCapitalFlowAdapter()
        binding.recyclerViewCapitalFlow.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = capitalAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * 加载数据：上涨/下跌热力图 + 板块资金流
     */
    private fun loadData() {
        binding.swipeRefresh.isRefreshing = true
        Thread {
            try {
                // 获取所有板块
                val allSectors = StockRepository.getGlobalSectors()
                val capitalFlows = StockRepository.getSectorCapitalFlow()

                // 分成上涨和下跌
                val upSectors = allSectors.filter { it.isUp }
                    .sortedByDescending { it.changePercent }
                    .take(16)
                val downSectors = allSectors.filter { !it.isUp }
                    .sortedBy { it.changePercent }
                    .take(16)

                activity?.runOnUiThread {
                    // 上涨热力图
                    heatUpAdapter.submitList(upSectors)
                    binding.tvUpCount.text = "${upSectors.size}个"

                    // 下跌热力图
                    heatDownAdapter.submitList(downSectors)
                    binding.tvDownCount.text = "${downSectors.size}个"

                    // 板块资金流列表
                    capitalAdapter.submitList(capitalFlows)

                    // 更新市场状态
                    updateMarketStatus()
                    binding.swipeRefresh.isRefreshing = false
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }.start()
    }

    /**
     * 更新市场状态（交易中/休市）
     */
    private fun updateMarketStatus() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val time = hour * 60 + minute

        val isWeekday = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
        val isMorningSession = time in 570..690 // 9:30-11:30
        val isAfternoonSession = time in 780..900 // 13:00-15:00

        binding.tvMarketStatus.text = if (isWeekday && (isMorningSession || isAfternoonSession)) {
            "交易中 · 实时更新"
        } else {
            "休市中 · 数据为收盘快照"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(refreshRunnable)
        _binding = null
    }
}
