package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RecipientAccountTest {
    @Test
    fun `it creates with valid account list`() {
        val account =
            RecipientAccount.of(
                Account("1234567890123456"),
                Account("9876543210987654"),
            )

        assertEquals("1234567890123456,9876543210987654", account.value)
    }

    @Test
    fun `it creates from single account`() {
        val account = RecipientAccount.of(Account("1234567890123456"))

        assertEquals("1234567890123456", account.value)
    }

    @Test
    fun `it creates from multiple accounts`() {
        val account =
            RecipientAccount.of(
                Account("1234567890123456"),
                Account("9876543210987654"),
            )

        assertEquals("1234567890123456,9876543210987654", account.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val account = RecipientAccount.of(Account("1234567890123456"))

        assertEquals("1234567890123456\n", account.toString())
    }

    @Test
    fun `it throws exception when no accounts provided`() {
        assertFailsWith<IllegalStateException> {
            RecipientAccount.of()
        }
    }

    @Test
    fun `it throws exception when exceeding max accounts`() {
        assertFailsWith<IllegalStateException> {
            RecipientAccount.of(*Array(21) { Account("1234567890123456") })
        }
    }

    @Test
    fun `it accepts twenty accounts at max total length`() {
        val account = RecipientAccount.of(*Array(20) { Account("1234567890123456") })

        assertEquals(339, account.value.length)
    }
}
