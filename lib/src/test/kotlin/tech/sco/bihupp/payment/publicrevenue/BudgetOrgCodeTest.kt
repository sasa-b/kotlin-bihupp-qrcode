package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.BudgetOrgCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BudgetOrgCodeTest {
    @Test
    fun `it creates with valid code`() {
        val code = BudgetOrgCode("1200200")

        assertEquals("1200200", code.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val code = BudgetOrgCode("1200200")

        assertEquals("1200200\n", code.toString())
    }

    @Test
    fun `it throws exception when exceeding 7 digits`() {
        assertFailsWith<IllegalStateException> {
            BudgetOrgCode("12002001")
        }
    }

    @Test
    fun `it throws exception when fewer than 7 digits`() {
        assertFailsWith<IllegalStateException> {
            BudgetOrgCode("120020")
        }
    }

    @Test
    fun `it throws exception when non-numeric characters are provided`() {
        assertFailsWith<IllegalStateException> {
            BudgetOrgCode("120@200")
        }
    }
}
