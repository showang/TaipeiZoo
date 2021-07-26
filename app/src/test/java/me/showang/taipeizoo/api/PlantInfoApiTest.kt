package me.showang.taipeizoo.api

import kotlinx.coroutines.runBlocking
import me.showang.respect.suspend
import org.junit.Test

class PlantInfoApiTest {

    @Test
    fun testRequest() {
        runBlocking {
            PlantInfoApi("兩棲爬蟲動物館").suspend()
        }.let(::println)
    }

}