package de.eso.pitestdemo

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class Issue17Test {
    @Test
    fun badCase() = runTest {
        // NOK: No Coverage for runTest + inline fun
        assertEquals(true, inlineFn())
    }

    @Test
    fun goodCase() = runTest {
        // OK: for runTest + "not inlined" fun
        assertEquals(true, notInlinedFun())
    }

    @Test
    fun goodCaseTwo() {
        // OK: for inlined fun (without runTest)
        assertEquals(true, inlineFnTwo())
    }
}