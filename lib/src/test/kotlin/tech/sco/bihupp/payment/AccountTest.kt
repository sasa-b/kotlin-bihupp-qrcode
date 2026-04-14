package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AccountTest {
    @Test
    fun `it creates with valid account number`() {
        val account = Account("1234567890123456")

        assertEquals("1234567890123456", account.value)
    }

    @Test
    fun `it accepts shorter account numbers`() {
        val account = Account("123456")

        assertEquals("123456", account.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val account = Account("1234567890123456")

        assertEquals("1234567890123456\n", account.toString())
    }

    @Test
    fun `it throws exception when exceeding max length`() {
        assertFailsWith<IllegalStateException> {
            Account("12345678901234567") // 17 chars
        }
    }

    @Test
    fun `it accepts account number at max length`() {
        val account = Account("1234567890123456")

        assertEquals(16, account.value.length)
    }

    @Test
    fun `it throws exception when invalid characters are provided`() {
        assertFailsWith<IllegalStateException> {
            Account("123456789@123456")
        }
    }
}
