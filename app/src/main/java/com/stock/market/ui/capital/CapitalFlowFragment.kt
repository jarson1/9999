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
import com.stock.market.network.StockRepository
import java.util.Calendar

/**
 * 资金流入板块 - 板块热力图 + 主力资金流入板块排行
 */
class CapitalFlowFragment : Fragment() {

    private var _binding: FragmentCapitalFlowBinding? = null
    private val binding get() = _binding!!
    private lateinit var heatAdapter: SectorHeatAdapter
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
        setupHeatMap()
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
     * 配置热力图网格
     */
    private fun setupHeatMap() {
        heatAdapter = SectorHeatAdapter()
        binding.recyclerViewHeat.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = heatAdapter
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
     * 加载数据：热力图 + 板块资金流
     */
    private fun loadData() {
        binding.swipeRefresh.isRefreshing = true
        Thread {
            try {
                // 并行加载两个数据
                val sectors = StockRepository.getGlobalSectors()
                val capitalFlows = StockRepository.getSectorCapitalFlow()

                activity?.runOnUiThread {
                    // 热力图取前24个板块
                    heatAdapter.submitList(sectors.take(24))
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
