package de.eso.pitestdemo

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class Issue18Test {
    @Test
    fun misleadingDescription() = runTest {
        // Misleading description: "invokeSuspend : remove call to ..."
        // invokeSuspend() is not visible to the developer, but rather Kotlin internals
        foo()
        assertEquals(1, 1)
    }
}