package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyTest {
    @Test
    fun `it converts to string that ands with lf char`() {
        assertEquals("BAM\n", Currency().toString())
    }
}
