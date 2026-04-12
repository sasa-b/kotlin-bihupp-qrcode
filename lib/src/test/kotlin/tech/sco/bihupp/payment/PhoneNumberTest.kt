package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PhoneNumberTest {
    @Test
    fun `creates with valid e164 phone number`() {
        val phone = PhoneNumber.of("+38761234567")

        assertEquals("+38761234567", phone.value)
    }

    @Test
    fun `converts to string that ends with lf char`() {
        val phone = PhoneNumber.of("+38761234567")

        assertEquals("+38761234567\n", phone.toString())
    }

    @Test
    fun `accepts max length phone number`() {
        val phone = PhoneNumber.of("+12345678901234") // 15 chars including +

        assertEquals("+12345678901234", phone.value)
    }

    @Test
    fun `strips spaces from phone number`() {
        val phone = PhoneNumber.of("+387 61 234 567")

        assertEquals("+38761234567", phone.value)
    }

    @Test
    fun `accepts various international country codes`() {
        assertEquals("+12025551234", PhoneNumber.of("+12025551234").value)    // US
        assertEquals("+447911123456", PhoneNumber.of("+447911123456").value)  // UK
        assertEquals("+38761234567", PhoneNumber.of("+38761234567").value)    // Bosnia
        assertEquals("+38591234567", PhoneNumber.of("+38591234567").value)    // Croatia
        assertEquals("+38161234567", PhoneNumber.of("+38161234567").value)    // Serbia
    }

    @Test
    fun `throws exception when missing leading plus`() {
        assertFailsWith<IllegalStateException> {
            PhoneNumber.of("38761234567")
        }
    }

    @Test
    fun `throws exception when exceeding max length`() {
        assertFailsWith<IllegalStateException> {
            PhoneNumber.of("+1234567890123456") // 17 chars
        }
    }

    @Test
    fun `throws exception when leading zeros without plus`() {
        assertFailsWith<IllegalStateException> {
            PhoneNumber.of("0038761234567")
        }
    }

    @Test
    fun `throws exception when invalid characters are provided`() {
        assertFailsWith<IllegalStateException> {
            PhoneNumber.of("+38761@234567")
        }
    }
}
