package com.demo.tbonestock.stock.repository.impl

import com.demo.tbonestock.stock.model.Stock
import com.demo.tbonestock.stock.repository.CustomStockElasticRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.Sort
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.data.elasticsearch.core.query.Query

class CustomStockElasticRepositoryImpl(
    private val reactiveElasticsearchTemplate: ReactiveElasticsearchTemplate
) : CustomStockElasticRepository {
    override suspend fun searchByName(keyword: String): List<Stock> {
        val where = Criteria.where("name").contains(keyword)
        val query = CriteriaQuery(where)
        return reactiveElasticsearchTemplate.search(query, Stock::class.java)
            .asFlow().toList().map {
                it.content
            }
    }

    override suspend fun findByUpperStock(): List<Stock>  {
        val where = Criteria.where("changeRate").greaterThanEqual("29.0")
            .and("change").`is`("RISE")
        val sort = Sort.by(Sort.Direction.DESC, "changeRate")
        val query = CriteriaQuery(where).addSort<Query>(sort)
        return reactiveElasticsearchTemplate.search(query, Stock::class.java)
            .asFlow().toList().map {
                it.content
            }
    }

    override suspend fun findByLowerStock(): List<Stock>  {
        val where = Criteria.where("changeRate").lessThanEqual("-29.0")
            .and("change").`is`("FALL")
        val sort = Sort.by(Sort.Direction.DESC, "changeRate")
        val query = CriteriaQuery(where).addSort<Query>(sort)
        return reactiveElasticsearchTemplate.search(query, Stock::class.java)
            .asFlow().toList().map {
                it.content
            }
    }
}