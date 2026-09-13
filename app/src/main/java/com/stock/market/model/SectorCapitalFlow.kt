package com.stock.market.model

/**
 * 板块资金流入数据模型
 */
data class SectorCapitalFlow(
    val rank: Int,
    val name: String,
    val code: String,
    val netInflow: Double,       // 主力净流入（亿元）
    val changePercent: Double,   // 板块涨跌幅
    val isUp: Boolean,
    val recentChangePercent: Double? = null,  // 近5日累计涨跌幅
    val recentIsUp: Boolean? = null
)
