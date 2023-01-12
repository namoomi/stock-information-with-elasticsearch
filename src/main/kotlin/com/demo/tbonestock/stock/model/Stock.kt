package com.demo.tbonestock.stock.model

import com.demo.tbonestock.stock.dto.CategoryType
import com.demo.tbonestock.stock.dto.ChangeType
import com.demo.tbonestock.stock.dto.MarketType
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "stock")
class Stock(
    @Id
    val id: String? = null,
    @Field(type = FieldType.Text)
    val category: CategoryType = CategoryType.STOCK,
    @Field(type = FieldType.Text)
    val marketType: MarketType,
    @Field(type = FieldType.Text)
    val sectorName: String,
    @Field(type = FieldType.Text)
    val sectorCode: String,
    @Field(type = FieldType.Text)
    val name: String,
    @Field(type = FieldType.Text)
    val code: String,
    @Field(type = FieldType.Text)
    val symbolCode: String,
    @Field(type = FieldType.Long)
    val price: Long,
    @Field(type = FieldType.Text)
    val change: ChangeType,
    @Field(type = FieldType.Long)
    val changePrice: Long,
    @Field(type = FieldType.Double)
    val changeRate: Double,
    @Field(type = FieldType.Long)
    val accTradeVolume: Long,
    @Field(type = FieldType.Long)
    val accTradePrice: Long,
    @Field(type = FieldType.Long)
    val marketCap: Long,
    @Field(type = FieldType.Long)
    val numOfStock: Long,
    @Field(type = FieldType.Text)
    val foreignRatio: String,
    @Field(type = FieldType.Text)
    val date: String
) {
    fun withId(id: String?) = Stock(
        id = "${date}_${code}",
        marketType = marketType,
        sectorName = sectorName,
        sectorCode = sectorCode,
        name = name,
        code = code,
        symbolCode = symbolCode,
        price = price,
        change = change,
        changePrice = changePrice,
        changeRate = changeRate,
        accTradeVolume = accTradeVolume,
        accTradePrice = accTradePrice,
        marketCap = marketCap,
        foreignRatio = foreignRatio,
        numOfStock = (marketCap / price),
        date = date
    )
}

