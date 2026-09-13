package com.stock.market.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.stock.market.model.CapitalFlow
import com.stock.market.model.HotStock
import com.stock.market.model.Sector
import com.stock.market.model.SectorCapitalFlow
import com.stock.market.utils.MockData

/**
 * 股票数据仓库
 * 统一管理真实 API 数据获取，失败时降级为模拟数据
 */
object StockRepository {

    private val gson = Gson()

    // 东方财富通用参数
    private const val EASTMONEY_UT = "bd1d9ddb04089700cf9c27f6f742628"

    // ==================== 全球产业板块数据 ====================

    /**
     * 获取行业板块数据（修复版）
     * 使用东方财富行业板块行情接口
     */
    fun getGlobalSectors(): List<Sector> {
        return try {
            val url = "https://push2.eastmoney.com/api/qt/clist/get?" +
                    "pn=1&pz=60&po=1&np=1&fltt=2&invt=2&fid=f12&fs=m:90+t:2" +
                    "&ut=$EASTMONEY_UT" +
                    "&fields=f12,f14,f2,f3,f4,f8,f20,f21,f128,f140,f141"
            val response = ApiClient.get(url) ?: return MockData.getGlobalSectors()

            val json = gson.fromJson(response, JsonObject::class.java)
            val data = json.getAsJsonObject("data") ?: return MockData.getGlobalSectors()
            val diff = data.getAsJsonArray("diff") ?: return MockData.getGlobalSectors()

            val sectors = mutableListOf<Sector>()
            val iconMap = mapOf(
                "半导体" to "🔬", "AI算力" to "🧠", "CPO" to "💡",
                "存储" to "💾", "数据中心" to "🗄️", "云计算" to "☁️",
                "商业航天" to "🚀", "卫星" to "🛰️", "机器人" to "🤖",
                "自动驾驶" to "🚗", "核电" to "⚛️", "电网" to "⚡",
                "军工" to "🛡️", "新能源" to "🔋", "光伏" to "☀️",
                "锂电池" to "🔌", "石油" to "🛢️", "天然气" to "🔥",
                "黄金" to "🏅", "银行" to "🏦", "生物医药" to "💊",
                "消费" to "🛒", "稀土" to "🧲", "有色金属" to "🟠",
                "证券" to "📈", "保险" to "🛡️", "房地产" to "🏠",
                "建筑" to "🏗️", "钢铁" to "⚙️", "煤炭" to "⬛",
                "电力" to "💡", "汽车" to "🚗", "家电" to "📺",
                "食品饮料" to "🍜", "农林牧渔" to "🌾", "纺织服装" to "👕",
                "医药商业" to "💊", "医疗服务" to "🏥", "医疗器械" to "🩺",
                "通信" to "📡", "电子" to "🔌", "计算机" to "💻",
                "传媒" to "🎬", "游戏" to "🎮", "旅游" to "✈️",
                "酒店餐饮" to "🏨", "交通运输" to "🚄", "公用事业" to "💧"
            )

            for (i in 0 until diff.size()) {
                val item = diff[i].asJsonObject
                val name = item.get("f14")?.asString ?: continue
                val change = item.get("f3")?.asDouble ?: 0.0
                val icon = iconMap[name] ?: "📊"
                sectors.add(Sector(name, icon, change, change >= 0))
            }

            if (sectors.isEmpty()) MockData.getGlobalSectors() else sectors.take(30)
        } catch (e: Exception) {
            e.printStackTrace()
            MockData.getGlobalSectors()
        }
    }

    // ==================== A股板块资金流入数据 ====================

    /**
     * 获取A股行业板块主力资金流入排名（真实数据）
     * 按主力净流入排序
     */
    fun getSectorCapitalFlow(): List<SectorCapitalFlow> {
        return try {
            val url = "https://push2.eastmoney.com/api/qt/clist/get?" +
                    "pn=1&pz=30&po=1&np=1&fltt=2&invt=2&fid=f62&fs=m:90+t:2" +
                    "&ut=$EASTMONEY_UT" +
                    "&fields=f12,f14,f2,f3,f62,f184,f66,f69,f72,f75,f78,f81,f84,f87,f124,f128,f140,f141"
            val response = ApiClient.get(url) ?: return MockData.getSectorCapitalFlow()

            val json = gson.fromJson(response, JsonObject::class.java)
            val data = json.getAsJsonObject("data") ?: return MockData.getSectorCapitalFlow()
            val diff = data.getAsJsonArray("diff") ?: return MockData.getSectorCapitalFlow()

            val flows = mutableListOf<SectorCapitalFlow>()
            for (i in 0 until diff.size()) {
                val item = diff[i].asJsonObject
                val code = item.get("f12")?.asString ?: continue
                val name = item.get("f14")?.asString ?: continue
                val netInflow = (item.get("f62")?.asDouble ?: 0.0) / 100000000 // 转换为亿元
                val changePercent = item.get("f3")?.asDouble ?: 0.0
                val recentChange = item.get("f184")?.asDouble

                flows.add(
                    SectorCapitalFlow(
                        rank = i + 1,
                        name = name,
                        code = code,
                        netInflow = Math.round(netInflow * 100.0) / 100.0,
                        changePercent = changePercent,
                        isUp = changePercent >= 0,
                        recentChangePercent = recentChange,
                        recentIsUp = recentChange?.let { it >= 0 }
                    )
                )
            }

            if (flows.isEmpty()) MockData.getSectorCapitalFlow() else flows
        } catch (e: Exception) {
            e.printStackTrace()
            MockData.getSectorCapitalFlow()
        }
    }

    // ==================== A股个股资金流入（保留，热股排名用） ====================

    /**
     * 获取A股个股主力资金流入排名（用于热股排名）
     */
    fun getAStockCapitalFlow(): List<CapitalFlow> {
        return try {
            val url = "https://push2.eastmoney.com/api/qt/clist/get?" +
                    "pn=1&pz=30&po=1&np=1&fltt=2&invt=2&fid=f62&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23" +
                    "&ut=$EASTMONEY_UT" +
                    "&fields=f12,f14,f2,f3,f62,f184,f66,f69,f72,f75,f78,f81,f84,f87"
            val response = ApiClient.get(url) ?: return MockData.getAStockCapitalFlow()

            val json = gson.fromJson(response, JsonObject::class.java)
            val data = json.getAsJsonObject("data") ?: return MockData.getAStockCapitalFlow()
            val diff = data.getAsJsonArray("diff") ?: return MockData.getAStockCapitalFlow()

            val flows = mutableListOf<CapitalFlow>()
            for (i in 0 until diff.size()) {
                val item = diff[i].asJsonObject
                val code = item.get("f12")?.asString ?: continue
                val name = item.get("f14")?.asString ?: continue
                val netInflow = (item.get("f62")?.asDouble ?: 0.0) / 100000000
                val changePercent = item.get("f3")?.asDouble ?: 0.0
                val price = item.get("f2")?.asDouble ?: 0.0
                val recentChange = item.get("f184")?.asDouble

                flows.add(
                    CapitalFlow(
                        rank = i + 1,
                        name = name,
                        code = code,
                        netInflow = Math.round(netInflow * 100.0) / 100.0,
                        changePercent = changePercent,
                        isUp = changePercent >= 0,
                        recentChangePercent = recentChange,
                        recentIsUp = recentChange?.let { it >= 0 }
                    )
                )
            }

            if (flows.isEmpty()) MockData.getAStockCapitalFlow() else flows
        } catch (e: Exception) {
            e.printStackTrace()
            MockData.getAStockCapitalFlow()
        }
    }

    // ==================== 美股资金流入数据（保留备用） ====================

    fun getUSStockCapitalFlow(): List<CapitalFlow> {
        return MockData.getUSStockCapitalFlow()
    }

    // ==================== 热股排名数据（纯A股） ====================

    /**
     * 获取A股热股排名（纯A股，按主力资金流入排序）
     */
    fun getHotStocks(): List<HotStock> {
        return try {
            val aStocks = getAStockCapitalFlow().take(20)

            val hotStocks = mutableListOf<HotStock>()
            aStocks.forEachIndexed { index, flow ->
                hotStocks.add(
                    HotStock(
                        rank = index + 1,
                        name = flow.name,
                        code = flow.code,
                        price = 0.0,
                        changePercent = flow.changePercent,
                        isUp = flow.isUp,
                        market = "A股",
                        heat = 10000 - index * 300
                    )
                )
            }

            if (hotStocks.isEmpty()) MockData.getHotStocks().filter { it.market == "A股" } else hotStocks
        } catch (e: Exception) {
            e.printStackTrace()
            MockData.getHotStocks().filter { it.market == "A股" }
        }
    }
}
