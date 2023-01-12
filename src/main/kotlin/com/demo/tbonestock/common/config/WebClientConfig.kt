package com.demo.tbonestock.common.config

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        val sslContext: SslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build()

        val httpClient = HttpClient.create().secure { t -> t.sslContext(sslContext) }
            .followRedirect(true)
            .responseTimeout(Duration.ofSeconds(10))

        val exchangeStrategies = ExchangeStrategies.builder().codecs {
            it.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)
        }.build()

        return WebClient.builder().exchangeStrategies(exchangeStrategies)
            .clientConnector(ReactorClientHttpConnector(httpClient)).build()
    }
}