package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PaymentReferenceTest {
    @Test
    fun `it creates with valid reference`() {
        val reference = PaymentReference("123456789")

        assertEquals("123456789", reference.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val reference = PaymentReference("123456789")

        assertEquals("123456789\n", reference.toString())
    }

    @Test
    fun `it throws exception when exceeding max length`() {
        assertFailsWith<IllegalStateException> {
            PaymentReference("1".repeat(31))
        }
    }

    @Test
    fun `it accepts reference at max length`() {
        val reference = PaymentReference("1".repeat(30))

        assertEquals(30, reference.value.length)
    }

    @Test
    fun `it accepts alphanumeric references`() {
        val reference = PaymentReference("INV2024-001")

        assertEquals("INV2024-001", reference.value)
    }

    @Test
    fun `it accepts allowed special characters`() {
        val reference = PaymentReference("REF-123/456")

        assertEquals("REF-123/456", reference.value)
    }

    @Test
    fun `it throws exception when invalid characters are provided`() {
        assertFailsWith<IllegalStateException> {
            PaymentReference("REF@123")
        }
    }
}
