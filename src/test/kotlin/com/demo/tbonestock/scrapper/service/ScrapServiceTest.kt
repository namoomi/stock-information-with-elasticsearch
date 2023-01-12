package com.demo.tbonestock.scrapper.service

import com.demo.tbonestock.TboneStockApplication
import com.demo.tbonestock.stock.dto.MarketType
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.v106.network.Network
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.nio.file.Paths
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TboneStockApplication::class])
internal class ScrapServiceTest @Autowired constructor(
    private val scrapService: ScrapService
) {
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

    val daumWebClient = WebClient.builder().exchangeStrategies(exchangeStrategies)
        .clientConnector(ReactorClientHttpConnector(httpClient)).build()

    @Test
    fun scrapTest() = runBlocking {
        val daum = Jsoup.connect("https://finance.daum.net/domestic/all_stocks?market=KOSPI")
            .timeout(5000)
            .method(Connection.Method.GET)
            .execute();
        log.info { daum.body() }
        log.info { daum.cookies() }
        log.info { daum.headers() }

        Jsoup.connect("https://finance.daum.net/api/quotes/sectors?fieldName=&order=&perPage=&market=KOSPI&page=&changes=UPPER_LIMIT%2CRISE%2CEVEN%2CFALL%2CLOWER_LIMIT")
            .timeout(5000)
            .method(Connection.Method.GET)
            .execute();

    }

    @Test
    fun extractSectorApiWithSelenium() = runBlocking {
        val options = ChromeOptions()
        //options.setBinary(path.toString())
        //options.addArguments("--start-maximized");          // 최대크기로
        options.addArguments("--headless");                 // Browser를 띄우지 않음
        //options.addArguments("--disable-gpu");              // GPU를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        //options.addArguments("--disable-logging");
        options.addArguments("--no-sandbox");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        //options.addArguments("--allowed-ips");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--single-process");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--disable-dev-shm-usage");
        options.setCapability("ignoreProtectedModeSettings", true)

        val path = Paths.get(System.getProperty("user.dir"), "src/main/resources/chromedriver")
        //System.setProperty("webdriver.chrome.driver", path.toString())

        val service = ChromeDriverService.Builder()
            .usingDriverExecutable(path.toFile())
            .usingAnyFreePort()
            .build()

        service.start()

        val driver = ChromeDriver(service, options)
        val devTool = driver.devTools
        devTool.createSession()
        devTool.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
        devTool.addListener(Network.requestWillBeSent()) { requestSent ->
            if (requestSent.request.url.contains("finance.daum.net/api/quotes/sectors")) {
                log.info { "Request URL => " + requestSent.request.url }
                log.info { "Request Method => " + requestSent.request.method }
                log.info { "Request Headers => " + requestSent.request.headers.toString() }
            }
        }

        devTool.addListener(Network.responseReceived()) { responseSent ->
            if (responseSent.response.url.contains("finance.daum.net/api/quotes/sectors")) {
                log.info { "Response URL => " + responseSent.response.url }
                log.info { "Response statusText => " + responseSent.response.statusText }
                //log.info { "body:" + devTool.send(Network.getResponseBody(responseSent.requestId)).body }
            }
        }

        driver.get("https://finance.daum.net/domestic/all_stocks?market=KOSPI")
    }

    @Test
    fun callService() = runBlocking {
        scrapService.scrap(MarketType.KOSPI)
    }
}