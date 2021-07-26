package me.showang.taipeizoo.api

import kotlinx.coroutines.runBlocking
import me.showang.respect.suspend
import org.junit.Test

class AreaIntroApiTest {

    @Test
    fun testRequest() {
        runBlocking {
            AreaIntroApi().suspend()
        }.let(::println)
    }

}