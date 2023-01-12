package com.demo.tbonestock.stock.controller

import com.demo.tbonestock.stock.dto.HipStockTabType
import com.demo.tbonestock.stock.dto.MarketType
import com.demo.tbonestock.stock.service.StockService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class StockHandler(
    private val stockService: StockService,
) {
    suspend fun tempSave(request: ServerRequest): ServerResponse {
        stockService.scrapAndSave(MarketType.KOSPI)
        return ServerResponse.ok().bodyValue("").awaitSingle()
    }

    suspend fun search(request: ServerRequest): ServerResponse {
        val keyword = request.queryParam("keyword").get()
        return ServerResponse.ok().bodyValue(stockService.search(keyword)).awaitSingle()
    }

    suspend fun hip(request: ServerRequest): ServerResponse {
        val tab = request.queryParam("tab").get()
        return ServerResponse.ok().bodyValue(stockService.hip(HipStockTabType.valueOf(tab))).awaitSingle()
    }
}