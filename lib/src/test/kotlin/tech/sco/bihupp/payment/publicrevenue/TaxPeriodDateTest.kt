package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.TaxPeriodDate
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaxPeriodDateTest {
    @Test
    fun `it creates from local date`() {
        val date = TaxPeriodDate.of(LocalDate.of(2024, 1, 15))

        assertEquals("15012024", date.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val date = TaxPeriodDate.of(LocalDate.of(2024, 1, 15))

        assertEquals("15012024\n", date.toString())
        assertTrue(date.toString().endsWith("\n"))
    }

    @Test
    fun `it pads single digit day and month with leading zeros`() {
        val date = TaxPeriodDate.of(LocalDate.of(2024, 3, 5))

        assertEquals("05032024", date.value)
    }
}
