package com.demo.tbonestock.stock.repository

import com.demo.tbonestock.stock.model.Stock

interface CustomStockElasticRepository {
    suspend fun searchByName(keyword: String): List<Stock>
    suspend fun findByUpperStock(): List<Stock>
    suspend fun findByLowerStock(): List<Stock>
}