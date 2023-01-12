package com.demo.tbonestock.common.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies


@Configuration
@EnableReactiveElasticsearchRepositories(
    basePackages = ["org.springframework.data.elasticsearch.repositories"]
)
class ElasticsearchConfig : AbstractReactiveElasticsearchConfiguration() {
    @Bean
    override fun reactiveElasticsearchClient(): ReactiveElasticsearchClient {
        val clientConfiguration: ClientConfiguration = ClientConfiguration.builder()
            .connectedTo("localhost:9200")
            .withWebClientConfigurer { webClient ->
                val exchangeStrategies = ExchangeStrategies.builder()
                    .codecs { configurer: ClientCodecConfigurer ->
                        configurer.defaultCodecs()
                            .maxInMemorySize(-1)
                    }
                    .build()
                webClient.mutate().exchangeStrategies(exchangeStrategies).build()
            }
            .build()
        return ReactiveRestClients.create(clientConfiguration)
    }
}