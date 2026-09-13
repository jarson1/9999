package com.stock.market.utils

import com.stock.market.model.CapitalFlow
import com.stock.market.model.HotStock
import com.stock.market.model.Sector
import com.stock.market.model.SectorCapitalFlow

/**
 * 模拟数据生成工具
 * 实际项目中可替换为真实 API 接口数据
 */
object MockData {

    /**
     * 全球产业板块数据
     */
    fun getGlobalSectors(): List<Sector> {
        return listOf(
            Sector("AI算力", "🧠", -0.01, false),
            Sector("CPO", "💡", -0.04, false),
            Sector("半导体", "🔬", 0.11, true),
            Sector("存储", "💾", -0.07, false),
            Sector("数据中心", "🗄️", 0.72, true),
            Sector("云计算", "☁️", -0.61, false),
            Sector("商业航天", "🚀", 0.08, true),
            Sector("卫星", "🛰️", 0.32, true),
            Sector("机器人", "🤖", -0.39, false),
            Sector("自动驾驶", "🚗", -0.12, false),
            Sector("核电", "⚛️", 0.38, true),
            Sector("电网", "⚡", 0.29, true),
            Sector("军工", "🛡️", 0.09, true),
            Sector("新能源", "🔋", 0.24, true),
            Sector("光伏", "☀️", 0.23, true),
            Sector("锂电池", "🔌", 0.89, true),
            Sector("石油", "🛢️", 0.32, true),
            Sector("天然气", "🔥", -0.38, false),
            Sector("铜/有色", "🟠", -1.30, false),
            Sector("黄金", "🏅", -0.07, false),
            Sector("银行金融", "🏦", 0.43, true),
            Sector("生物医药", "💊", -0.03, false),
            Sector("消费", "🛒", 0.13, true),
            Sector("稀土", "🧲", 0.45, true)
        )
    }

    /**
     * A股主力资金流入排名
     */
    fun getAStockCapitalFlow(): List<CapitalFlow> {
        return listOf(
            CapitalFlow(1, "贵州茅台", "600519", 12.56, 2.35, true, 5.67, true),
            CapitalFlow(2, "宁德时代", "300750", 10.89, 3.12, true, 8.45, true),
            CapitalFlow(3, "比亚迪", "002594", 9.45, 1.87, true, 4.23, true),
            CapitalFlow(4, "中芯国际", "688981", 8.23, 4.56, true, 12.34, true),
            CapitalFlow(5, "隆基绿能", "601012", 7.67, -1.23, false, -3.45, false),
            CapitalFlow(6, "招商银行", "600036", 6.89, 0.89, true, 2.15, true),
            CapitalFlow(7, "中国平安", "601318", 6.34, 1.45, true, 3.78, true),
            CapitalFlow(8, "五粮液", "000858", 5.78, 2.10, true, 6.89, true),
            CapitalFlow(9, "美的集团", "000333", 5.23, 0.67, true, 1.56, true),
            CapitalFlow(10, "恒瑞医药", "600276", 4.89, -0.56, false, -2.34, false),
            CapitalFlow(11, "海康威视", "002415", 4.56, 1.23, true, 5.12, true),
            CapitalFlow(12, "万华化学", "600309", 4.12, -0.89, false, -1.78, false),
            CapitalFlow(13, "紫金矿业", "601899", 3.89, 2.45, true, 7.56, true),
            CapitalFlow(14, "药明康德", "603259", 3.56, -1.67, false, -5.23, false),
            CapitalFlow(15, "三一重工", "600031", 3.23, 0.34, true, 1.89, true)
        )
    }

    /**
     * 美股主力资金流入排名
     */
    fun getUSStockCapitalFlow(): List<CapitalFlow> {
        return listOf(
            CapitalFlow(1, "NVIDIA", "NVDA", 45.67, 5.23, true),
            CapitalFlow(2, "Apple", "AAPL", 38.45, 2.15, true),
            CapitalFlow(3, "Microsoft", "MSFT", 32.89, 1.87, true),
            CapitalFlow(4, "Tesla", "TSLA", 28.56, -3.45, false),
            CapitalFlow(5, "Amazon", "AMZN", 25.34, 2.67, true),
            CapitalFlow(6, "Alphabet", "GOOGL", 22.78, 1.45, true),
            CapitalFlow(7, "Meta", "META", 19.89, 3.21, true),
            CapitalFlow(8, "AMD", "AMD", 17.45, 6.78, true),
            CapitalFlow(9, "Netflix", "NFLX", 15.23, -1.23, false),
            CapitalFlow(10, "JPMorgan", "JPM", 13.89, 0.89, true),
            CapitalFlow(11, "Visa", "V", 12.56, 1.12, true),
            CapitalFlow(12, "Johnson & Johnson", "JNJ", 11.23, -0.45, false),
            CapitalFlow(13, "Walmart", "WMT", 10.89, 0.67, true),
            CapitalFlow(14, "Procter & Gamble", "PG", 9.78, -0.23, false),
            CapitalFlow(15, "Disney", "DIS", 8.56, 2.34, true)
        )
    }

    /**
     * 热股排名（综合A股和美股）
     */
    fun getHotStocks(): List<HotStock> {
        return listOf(
            HotStock(1, "NVIDIA", "NVDA", 875.32, 5.23, true, "美股", 9856),
            HotStock(2, "贵州茅台", "600519", 1689.00, 2.35, true, "A股", 9567),
            HotStock(3, "Tesla", "TSLA", 245.67, -3.45, false, "美股", 9234),
            HotStock(4, "宁德时代", "300750", 215.80, 3.12, true, "A股", 8976),
            HotStock(5, "Apple", "AAPL", 189.45, 2.15, true, "美股", 8765),
            HotStock(6, "比亚迪", "002594", 256.70, 1.87, true, "A股", 8543),
            HotStock(7, "Microsoft", "MSFT", 415.23, 1.87, true, "美股", 8234),
            HotStock(8, "中芯国际", "688981", 58.90, 4.56, true, "A股", 7987),
            HotStock(9, "AMD", "AMD", 167.89, 6.78, true, "美股", 7654),
            HotStock(10, "隆基绿能", "601012", 23.45, -1.23, false, "A股", 7345),
            HotStock(11, "Amazon", "AMZN", 178.56, 2.67, true, "美股", 7123),
            HotStock(12, "招商银行", "600036", 38.90, 0.89, true, "A股", 6890),
            HotStock(13, "Meta", "META", 498.76, 3.21, true, "美股", 6678),
            HotStock(14, "中国平安", "601318", 48.56, 1.45, true, "A股", 6456),
            HotStock(15, "Alphabet", "GOOGL", 167.45, 1.45, true, "美股", 6234),
            HotStock(16, "五粮液", "000858", 156.80, 2.10, true, "A股", 6012),
            HotStock(17, "Netflix", "NFLX", 612.34, -1.23, false, "美股", 5890),
            HotStock(18, "美的集团", "000333", 68.90, 0.67, true, "A股", 5678),
            HotStock(19, "JPMorgan", "JPM", 198.67, 0.89, true, "美股", 5456),
            HotStock(20, "恒瑞医药", "600276", 45.67, -0.56, false, "A股", 5234)
        )
    }

    /**
     * A股板块资金流入模拟数据
     */
    fun getSectorCapitalFlow(): List<SectorCapitalFlow> {
        return listOf(
            SectorCapitalFlow(1, "半导体", "BK1036", 45.67, 3.45, true, 8.90, true),
            SectorCapitalFlow(2, "新能源", "BK0478", 38.45, 2.15, true, 5.67, true),
            SectorCapitalFlow(3, "医药生物", "BK0465", 32.89, 1.87, true, 3.45, true),
            SectorCapitalFlow(4, "电力设备", "BK0464", 28.56, -1.23, false, -2.34, false),
            SectorCapitalFlow(5, "电子", "BK0459", 25.34, 4.56, true, 10.23, true),
            SectorCapitalFlow(6, "计算机", "BK0447", 22.78, 2.67, true, 6.78, true),
            SectorCapitalFlow(7, "通信", "BK0448", 19.89, 1.45, true, 4.56, true),
            SectorCapitalFlow(8, "汽车", "BK0481", 17.45, 3.21, true, 7.89, true),
            SectorCapitalFlow(9, "食品饮料", "BK0438", 15.23, -0.89, false, -1.23, false),
            SectorCapitalFlow(10, "银行", "BK0475", 13.89, 0.89, true, 2.34, true),
            SectorCapitalFlow(11, "非银金融", "BK0473", 12.56, 1.12, true, 3.45, true),
            SectorCapitalFlow(12, "房地产", "BK0451", 11.23, -2.45, false, -5.67, false),
            SectorCapitalFlow(13, "建筑材料", "BK0439", 10.89, 0.67, true, 1.89, true),
            SectorCapitalFlow(14, "钢铁", "BK0479", 9.78, -0.23, false, -0.89, false),
            SectorCapitalFlow(15, "有色金属", "BK0478", 8.56, 2.34, true, 4.56, true),
            SectorCapitalFlow(16, "煤炭", "BK0437", 7.45, 1.56, true, 2.78, true),
            SectorCapitalFlow(17, "石油石化", "BK0462", 6.34, -0.78, false, -1.45, false),
            SectorCapitalFlow(18, "公用事业", "BK0442", 5.23, 0.45, true, 1.23, true),
            SectorCapitalFlow(19, "交通运输", "BK0425", 4.12, 1.78, true, 3.56, true),
            SectorCapitalFlow(20, "传媒", "BK0441", 3.89, -1.34, false, -3.21, false)
        )
    }
}
