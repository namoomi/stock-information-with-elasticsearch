package com.demo.tbonestock.sector.service

import com.demo.tbonestock.sector.model.Sector
import com.demo.tbonestock.sector.repository.SectorElasticRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service

@Service
class SectorService(
    private val sectorElasticRepository: SectorElasticRepository
) {
    suspend fun search(keyword: String): List<Sector> {
        return sectorElasticRepository.findBySectorName(keyword).asFlow().toList()
    }

    suspend fun allSectors(): List<Sector> {
        return sectorElasticRepository.findAll().asFlow().toList()
    }
}