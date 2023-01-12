package com.demo.tbonestock.stock.service

import com.demo.tbonestock.scrapper.service.ScrapService
import com.demo.tbonestock.sector.repository.SectorElasticRepository
import com.demo.tbonestock.stock.dto.HipStockTabType
import com.demo.tbonestock.stock.dto.MarketType
import com.demo.tbonestock.stock.model.Stock
import com.demo.tbonestock.stock.repository.StockElasticRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class StockService(
    private val scrapService: ScrapService,
    private val sectorElasticRepository: SectorElasticRepository,
    private val stockRepository: StockElasticRepository,
) {
    suspend fun scrapAndSave(marketType: MarketType) {
        val allRawData = scrapService.scrap(marketType)
        val stocks = mutableListOf<Stock>()

        val sectors = allRawData.data.map { sector ->
            stocks.addAll(sector.includedStocks.map { stock ->
                Stock(
                    id = "${sector.date}_${stock.code}",
                    marketType = marketType,
                    sectorName = sector.sectorName,
                    sectorCode = sector.sectorCode,
                    name = stock.name,
                    code = stock.code,
                    symbolCode = stock.symbolCode,
                    price = stock.tradePrice,
                    change = stock.change,
                    changePrice = stock.changePrice,
                    changeRate = stock.changeRate,
                    accTradeVolume = stock.accTradeVolume,
                    accTradePrice = stock.accTradePrice,
                    marketCap = stock.marketCap,
                    foreignRatio = stock.foreignRatio,
                    numOfStock = (stock.marketCap / stock.tradePrice),
                    date = sector.date
                )
            })
            sector.toEntity(marketType)
        }

        sectorElasticRepository.saveAll(sectors).asFlow().toList()
        stockRepository.saveAll(stocks).asFlow().toList()
    }

    suspend fun search(keyword: String): List<Stock> {
        return stockRepository.searchByName(keyword)
    }

    //paging생략
    suspend fun hip(tab: HipStockTabType): List<Stock>  {
        return when(tab) {
            HipStockTabType.RISE -> stockRepository.findByUpperStock()
            HipStockTabType.FALL -> stockRepository.findByLowerStock()
            HipStockTabType.DRAMATIC_RISE -> stockRepository.findByChangeOrderByChangeRateDesc().asFlow().toList()
            else -> emptyList()
        }
    }
}