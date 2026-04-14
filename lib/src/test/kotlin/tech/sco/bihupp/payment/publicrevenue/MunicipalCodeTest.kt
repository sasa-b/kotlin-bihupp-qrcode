package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.MunicipalCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MunicipalCodeTest {
    @Test
    fun `it creates with valid code`() {
        val code = MunicipalCode("077")

        assertEquals("077", code.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val code = MunicipalCode("077")

        assertEquals("077\n", code.toString())
    }

    @Test
    fun `it throws exception when exceeding 3 digits`() {
        assertFailsWith<IllegalStateException> {
            MunicipalCode("0771")
        }
    }

    @Test
    fun `it throws exception when fewer than 3 digits`() {
        assertFailsWith<IllegalStateException> {
            MunicipalCode("07")
        }
    }

    @Test
    fun `it throws exception when non-numeric characters are provided`() {
        assertFailsWith<IllegalStateException> {
            MunicipalCode("07@")
        }
    }
}
