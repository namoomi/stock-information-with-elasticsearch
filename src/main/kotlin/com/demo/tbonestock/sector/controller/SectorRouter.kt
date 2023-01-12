package com.demo.tbonestock.sector.controller

import com.demo.tbonestock.stock.controller.StockHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SectorRouter {
    val basePath = "/api/v1/sectors"

    @Bean
    fun sectorRoute(handler: SectorHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("", handler::all)
                GET("/search", handler::search)
            }
        }
    }
}