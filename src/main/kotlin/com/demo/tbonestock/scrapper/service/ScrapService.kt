package com.demo.tbonestock.scrapper.service

import com.demo.tbonestock.stock.dto.AllStocks
import com.demo.tbonestock.stock.dto.MarketType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.v106.network.Network
import org.springframework.stereotype.Service
import java.nio.file.Paths
import java.util.*

private val log = KotlinLogging.logger {}

@Service
class ScrapService {
    //todo move to properties or yaml
    private val DAUM_PAGE = "https://finance.daum.net/domestic/all_stocks?market="
    private val ALL_STOCK_JSON_URL = "finance.daum.net/api/quotes/sectors"

    suspend fun scrap(marketType: MarketType): AllStocks {
        val extracted = crawling { driver ->
            var result: String? = null
            val devTool = driver.devTools
            devTool.createSession()
            devTool.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))

            devTool.addListener(Network.responseReceived()) { responseSent ->
                if (responseSent.response.url.contains(ALL_STOCK_JSON_URL)) {
                    result = devTool.send(Network.getResponseBody(responseSent.requestId)).body
                }
            }
            driver.get("${DAUM_PAGE}${marketType.name}")
            result
        } as String?

        //todo mapper 분리
        val objectMapper = ObjectMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(2048)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        return objectMapper.readValue(extracted, AllStocks::class.java)
    }
}

private suspend fun crawling(
    action: suspend (driver: ChromeDriver) -> Any?
): Any? {
    val options = ChromeOptions()
        .addArguments("--headless")
        .addArguments("--no-sandbox")
        .addArguments("--disable-gpu")
        .addArguments("--disable-logging")
        .addArguments("--single-process")
        .addArguments("--disable-dev-shm-usage")
    options.setCapability("ignoreProtectedModeSettings", true)

    val path = Paths.get(System.getProperty("user.dir"), "src/main/resources/chromedriver")
    val service = ChromeDriverService.Builder()
        .usingDriverExecutable(path.toFile())
        .usingAnyFreePort()
        .build()
    try {
        service.start()

        val driver = ChromeDriver(service, options)
        return action(driver)
    } catch (_: Exception) {
        //logging
        return null
    } finally {
        service.stop()
    }
}
