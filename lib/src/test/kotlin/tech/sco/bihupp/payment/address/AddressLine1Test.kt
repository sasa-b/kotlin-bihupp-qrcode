package tech.sco.bihupp.payment.address

import tech.sco.bihupp.payment.AddressLine1
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddressLine1Test {
    @Test
    fun `creates with valid street and number`() {
        val address = AddressLine1.of("Ulica Meše Selimovića", "12")

        assertEquals("Ulica Meše Selimovića 12", address.value)
    }

    @Test
    fun `converts to string that ends with newline`() {
        val addressLine1 = AddressLine1.of("Ulica Kralja Petra 1. Karađorđevića", "3")

        assertEquals("Ulica Kralja Petra 1. Karađorđevića 3\n", addressLine1.toString())
    }

    @Test
    fun `throws when value exceeds max length`() {
        // "a" * 50 + " " + "b" = 52 chars, exceeds limit of 50
        val exception =
            assertFailsWith<IllegalStateException> {
                AddressLine1.of("a".repeat(50), "b")
            }
        assertEquals("AddressLine1 exceeds maximum length of ${AddressLine1.MAX_LENGTH}, got 52.", exception.message)
    }

    @Test
    fun `accepts value exactly at max length`() {
        // "a" * 45 + " " + "123" = 49 chars
        val street = "a".repeat(45)
        val number = "123"

        val addressLine1 = AddressLine1.of(street, number)

        assertEquals("$street $number", addressLine1.value)
    }

    @Test
    fun `throws when value contains invalid characters`() {
        assertFailsWith<IllegalStateException> {
            AddressLine1.of("Main @Street", "123")
        }
    }
}
