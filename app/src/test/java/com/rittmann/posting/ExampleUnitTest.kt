package com.rittmann.posting

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val a: String? = "b" // "a" or null is true
        if (a?.equals("a") == null) {
            assertTrue(true)
        } else
            fail()
    }
}
