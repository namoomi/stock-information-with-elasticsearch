package com.demo.tbonestock.stock.repository

import com.demo.tbonestock.stock.model.Stock
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import org.springframework.data.repository.Repository
import reactor.core.publisher.Flux

interface StockElasticRepository : ReactiveElasticsearchRepository<Stock, String>, CustomStockElasticRepository {
    suspend fun findByChangeOrderByChangeRateDesc(change: String = "RISE"): Flux<Stock>
}