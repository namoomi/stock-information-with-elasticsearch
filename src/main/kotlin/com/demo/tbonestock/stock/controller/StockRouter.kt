package com.demo.tbonestock.stock.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class StockRouter {
    val basePath = "/api/v1/stocks"

    @Bean
    fun stockRoute(handler: StockHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/", handler::tempSave)
                GET("/search", handler::search)
                GET("/hip", handler::hip)
            }
        }
    }
}