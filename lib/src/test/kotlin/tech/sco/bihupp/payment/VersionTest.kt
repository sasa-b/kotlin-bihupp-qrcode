package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VersionTest {
    @Test
    fun `it creates with valid version`() {
        val version = Version()

        assertEquals("BIHUPP10", version.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val version = Version()

        assertEquals("BIHUPP10\n", version.toString())
        assertTrue(version.toString().endsWith("\n"))
    }
}
