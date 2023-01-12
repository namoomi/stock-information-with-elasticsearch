package com.demo.tbonestock.stock.dto

import com.demo.tbonestock.sector.model.Sector
import java.math.BigDecimal

data class AllStocks(
    val data: List<SectorDto>
)

data class StockDto(
    val name: String,
    val code: String,
    val symbolCode: String,
    val tradePrice: Long,
    val change: ChangeType,
    val changePrice: Long,
    val changeRate: Double,
    val accTradeVolume: Long,
    val accTradePrice: Long,
    val marketCap: Long,
    val foreignRatio: String
)

data class SectorDto(
    val symbolCode: String,
    val code: String,
    val sectorCode: String,
    val sectorName: String,
    val date: String,
    val market: MarketType,
    val change: ChangeType,
    val changePrice: BigDecimal,
    val changeRate: Double,
    val tradePrice: BigDecimal,
    val prevClosingPrice: BigDecimal,
    val accTradeVolume: Long,
    val accTradePrice: Long,
    val includedStocks: List<StockDto> = emptyList(),
    val institutionStraightPurchasePrice: Long,
    val foreignStraightPurchasePrice: Long,
    val chartSlideImage: String?,
) {
    fun toEntity(marketType: MarketType) = Sector(
        id = "${date}_${code}",
        symbolCode = symbolCode,
        marketType = marketType,
        code = code,
        sectorCode = sectorCode,
        sectorName = sectorName,
        date = date,
        market = market,
        change = change,
        changePrice = changePrice,
        changeRate = changeRate,
        tradePrice = tradePrice,
        prevClosingPrice = prevClosingPrice,
        accTradeVolume = accTradeVolume,
        accTradePrice = accTradePrice,
        //includedStocks = includedStocks.map { it.toEntity() },
        institutionStraightPurchasePrice = institutionStraightPurchasePrice,
        foreignStraightPurchasePrice = foreignStraightPurchasePrice,
        chartSlideImage = chartSlideImage,
    )
}

enum class ChangeType {
    EVEN, RISE, FALL, UPPER_LIMIT
}

enum class MarketType {
    KOSPI, KOSDAQ
}

enum class CategoryType {
    STOCK, SECTOR
}

enum class HipStockTabType {
    RISE, FALL, DRAMATIC_RISE, DRAMATIC_FALL
}