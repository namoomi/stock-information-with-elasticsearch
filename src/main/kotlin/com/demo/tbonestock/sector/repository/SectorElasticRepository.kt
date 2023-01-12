package com.demo.tbonestock.sector.repository

import com.demo.tbonestock.sector.model.Sector
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import reactor.core.publisher.Flux

interface SectorElasticRepository : ReactiveElasticsearchRepository<Sector, String> {
    suspend fun findBySectorName(keyword: String): Flux<Sector>
}