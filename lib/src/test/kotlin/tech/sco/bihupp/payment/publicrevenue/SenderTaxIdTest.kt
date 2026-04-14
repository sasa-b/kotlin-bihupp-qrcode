package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.SenderTaxId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SenderTaxIdTest {
    @Test
    fun `it creates with valid jmbg`() {
        val taxId = SenderTaxId("0101990123456")

        assertEquals("0101990123456", taxId.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val taxId = SenderTaxId("0101990123456")

        assertEquals("0101990123456\n", taxId.toString())
    }

    @Test
    fun `it throws exception when fewer than 13 digits`() {
        assertFailsWith<IllegalStateException> {
            SenderTaxId("010199012345")
        }
    }

    @Test
    fun `it throws exception when more than 13 digits`() {
        assertFailsWith<IllegalStateException> {
            SenderTaxId("01019901234567")
        }
    }

    @Test
    fun `it throws exception when non-numeric characters are provided`() {
        assertFailsWith<IllegalStateException> {
            SenderTaxId("010199012345@")
        }
    }

    @Test
    fun `it throws exception when empty value is provided`() {
        assertFailsWith<IllegalStateException> {
            SenderTaxId("")
        }
    }
}
