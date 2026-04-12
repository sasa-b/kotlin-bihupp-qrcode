package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentPriorityTest {
    @Test
    fun `creates with valid default priority`() {
        val line = PaymentPriority()

        assertEquals("N", line.value)
    }

    @Test
    fun `creates from regular payment type`() {
        val paymentPriority = PaymentPriority.regular()

        assertEquals("N", paymentPriority.value)
    }

    @Test
    fun `creates from urgent payment type`() {
        val paymentPriority = PaymentPriority.urgent()

        assertEquals("D", paymentPriority.value)
    }

    @Test
    fun `converts to string that ends with lf char`() {
        val paymentPriority = PaymentPriority.urgent()

        assertEquals("D\n", paymentPriority.toString())
    }
}
