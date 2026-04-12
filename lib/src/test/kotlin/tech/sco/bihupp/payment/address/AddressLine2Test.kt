package tech.sco.bihupp.payment.address

import tech.sco.bihupp.payment.AddressLine2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddressLine2Test {
    @Test
    fun `creates with valid postal code and city`() {
        val address = AddressLine2.of("78000", "Banja Luka")

        assertEquals("78000 Banja Luka", address.value)
    }

    @Test
    fun `converts to string that ends with newline`() {
        val addressLine2 = AddressLine2.of("78000", "Banja Luka")

        assertEquals("78000 Banja Luka\n", addressLine2.toString())
    }

    @Test
    fun `throws when value exceeds max length`() {
        // "78000" + " " + "a" * 30 = 36 chars, exceeds limit of 25
        val exception =
            assertFailsWith<IllegalStateException> {
                AddressLine2.of("78000", "a".repeat(30))
            }
        assertEquals("AddressLine2 exceeds maximum length of ${AddressLine2.MAX_LENGTH}, got 36.", exception.message)
    }

    @Test
    fun `accepts value exactly at max length`() {
        // "78000" + " " + "a" * 19 = 25 chars
        val postcode = "78000"
        val town = "a".repeat(19)

        val addressLine2 = AddressLine2.of(postcode, town)

        assertEquals("$postcode $town", addressLine2.value)
    }

    @Test
    fun `throws when value contains invalid characters`() {
        assertFailsWith<IllegalStateException> {
            AddressLine2.of("71000", "Sarajevo@City")
        }
    }
}
