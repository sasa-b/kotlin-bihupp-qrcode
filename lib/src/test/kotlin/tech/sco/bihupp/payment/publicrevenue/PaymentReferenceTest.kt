package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.PublicRevenueInstruction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PaymentReferenceTest {
    @Test
    fun `it creates with valid reference`() {
        val reference = PublicRevenueInstruction.PaymentReference("7110578163")

        assertEquals("7110578163", reference.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val reference = PublicRevenueInstruction.PaymentReference("7110578163")

        assertEquals("7110578163\n", reference.toString())
    }

    @Test
    fun `it throws exception when value is not ten digits`() {
        assertFailsWith<IllegalStateException> {
            PublicRevenueInstruction.PaymentReference("12345678901")
        }
    }

    @Test
    fun `it throws exception when value is fewer than ten digits`() {
        assertFailsWith<IllegalStateException> {
            PublicRevenueInstruction.PaymentReference("711057816")
        }
    }

    @Test
    fun `it throws exception when value contains non-digits`() {
        assertFailsWith<IllegalStateException> {
            PublicRevenueInstruction.PaymentReference("REF@123456")
        }
    }
}
