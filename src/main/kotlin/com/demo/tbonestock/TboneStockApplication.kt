package com.demo.tbonestock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories

@SpringBootApplication
class TboneStockApplication

fun main(args: Array<String>) {
    runApplication<TboneStockApplication>(*args)
}
