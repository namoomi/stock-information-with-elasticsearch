package com.demo.tbonestock.sector.controller

import com.demo.tbonestock.sector.service.SectorService
import com.demo.tbonestock.stock.service.StockService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class SectorHandler(
    private val sectorService: SectorService
) {
    suspend fun search(request: ServerRequest): ServerResponse {
        val keyword = request.queryParam("keyword").get()
        return ServerResponse.ok().bodyValue(sectorService.search(keyword)).awaitSingle()
    }

    suspend fun all(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyValue(sectorService.allSectors()).awaitSingle()
    }
}