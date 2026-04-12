package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AmountTest {
    @Test
    fun `creates with valid amount`() {
        val amount = Amount.of("10000")

        assertEquals("000000000010000", amount.value)
    }

    @Test
    fun `converts to string that ends with newline`() {
        val amount = Amount.of("10000")

        assertEquals("000000000010000\n", amount.toString())
    }

    @Test
    fun `throws when exceeding max length`() {
        // 16 digits — padStart does not truncate, so the init check fails
        assertFailsWith<IllegalStateException> {
            Amount.of("9".repeat(16))
        }
    }

    @Test
    fun `accepts amount exactly at max length`() {
        val amount = Amount.of("9".repeat(15))

        assertEquals(Amount.MAX_LENGTH, amount.value.length)
    }

    @Test
    fun `accepts leading zero number amounts`() {
        val amount = Amount.of("0100")

        assertEquals("000000000000100", amount.value)
    }

    @Test
    fun `accepts decimal amounts by stripping the decimal point`() {
        val amount = Amount.of("100.00")

        assertEquals("000000000010000", amount.value)
    }

    @Test
    fun `creates from int`() {
        val amount = Amount.of(10000)

        assertEquals("000000000010000", amount.value)
    }

    @Test
    fun `creates from double`() {
        val amount = Amount.of(123.45)

        assertEquals("000000000012345", amount.value)
    }

    @Test
    fun `throws when value contains invalid characters`() {
        assertFailsWith<IllegalStateException> {
            Amount.of("100@00")
        }
    }
}
