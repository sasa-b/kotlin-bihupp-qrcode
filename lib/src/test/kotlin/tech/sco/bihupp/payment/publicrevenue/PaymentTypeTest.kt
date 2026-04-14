package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.PaymentType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PaymentTypeTest {
    @Test
    fun `it creates with valid payment type`() {
        val paymentType = PaymentType("3")

        assertEquals("3", paymentType.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val paymentType = PaymentType("3")

        assertEquals("3\n", paymentType.toString())
    }

    @Test
    fun `it throws exception when non-numeric characters are provided`() {
        assertFailsWith<IllegalStateException> {
            PaymentType("a")
        }
    }

    @Test
    fun `it throws exception when multi-digit value is provided`() {
        assertFailsWith<IllegalStateException> {
            PaymentType("12")
        }
    }

    @Test
    fun `it throws exception when empty value is provided`() {
        assertFailsWith<IllegalStateException> {
            PaymentType("")
        }
    }
}
