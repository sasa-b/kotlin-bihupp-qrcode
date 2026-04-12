package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PaymentPurposeTest {
    @Test
    fun `creates with valid purpose`() {
        val purpose = PaymentPurpose.of("Invoice payment")

        assertEquals("Invoice payment", purpose.value)
    }

    @Test
    fun `converts to string that ends with newline`() {
        val purpose = PaymentPurpose.of("Invoice payment")

        assertEquals("Invoice payment\n", purpose.toString())
    }

    @Test
    fun `throws when value exceeds max length`() {
        assertFailsWith<IllegalStateException> {
            PaymentPurpose.of("a".repeat(111))
        }
    }

    @Test
    fun `accepts value exactly at max length`() {
        val purpose = PaymentPurpose.of("a".repeat(110))

        assertEquals(PaymentPurpose.MAX_LENGTH, purpose.value.length)
    }

    @Test
    fun `normalizes newlines to spaces`() {
        val purpose = PaymentPurpose.of("Payment\n(invoice 123)\n-\ndiscount")

        assertEquals("Payment (invoice 123) - discount", purpose.value)
    }

    @Test
    fun `throws when value contains invalid characters`() {
        assertFailsWith<IllegalStateException> {
            PaymentPurpose.of("Invoice #123")
        }
    }
}
