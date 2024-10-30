package de.eso.pitestdemo

import org.junit.Assert.assertEquals
import org.junit.Test

class SomeMethodTest {

    @Test
    fun dummyTest() {
        assertEquals("a", someMethod(true))
    }

    @Test
    fun dummyTest2() {
        assertEquals("b", someMethod(false))
    }
}