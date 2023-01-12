package com.demo.tbonestock.sector.model

import com.demo.tbonestock.stock.dto.CategoryType
import com.demo.tbonestock.stock.dto.ChangeType
import com.demo.tbonestock.stock.dto.MarketType
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.math.BigDecimal

@Document(indexName = "sector")
class Sector(
    @Id
    val id: String? = null,
    @Field(type = FieldType.Text)
    val category: CategoryType = CategoryType.SECTOR,
    @Field(type = FieldType.Text)
    val marketType: MarketType,
    @Field(type = FieldType.Text)
    val symbolCode: String,
    @Field(type = FieldType.Text)
    val code: String,
    @Field(type = FieldType.Text)
    val sectorCode: String,
    @Field(type = FieldType.Text)
    val sectorName: String,
    @Field(type = FieldType.Text)
    val date: String,
    @Field(type = FieldType.Text)
    val market: MarketType,
    @Field(type = FieldType.Text)
    val change: ChangeType,
    @Field(type = FieldType.Double)
    val changePrice: BigDecimal,
    @Field(type = FieldType.Double)
    val changeRate: Double,
    @Field(type = FieldType.Double)
    val tradePrice: BigDecimal,
    @Field(type = FieldType.Double)
    val prevClosingPrice: BigDecimal,
    @Field(type = FieldType.Long)
    val accTradeVolume: Long,
    @Field(type = FieldType.Long)
    val accTradePrice: Long,
    /*@Field(type = FieldType.Nested, includeInParent = true)
    val includedStocks: List<Stock> = emptyList(),*/
    @Field(type = FieldType.Long)
    val institutionStraightPurchasePrice: Long,
    @Field(type = FieldType.Long)
    val foreignStraightPurchasePrice: Long,
    @Field(type = FieldType.Text)
    val chartSlideImage: String?,
) {
    fun withId(id: String?): Sector = Sector(
        symbolCode = symbolCode,
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
        //includedStocks = includedStocks,
        institutionStraightPurchasePrice = institutionStraightPurchasePrice,
        foreignStraightPurchasePrice = foreignStraightPurchasePrice,
        chartSlideImage = chartSlideImage,
        id = "${date}_${code}",
        marketType = marketType
    )
}