package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.RevenueType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RevenueTypeTest {
    @Test
    fun `it creates with valid revenue type`() {
        val revenueType = RevenueType("712115")

        assertEquals("712115", revenueType.value)
    }

    @Test
    fun `it converts to string that ends with lf char`() {
        val revenueType = RevenueType("712115")

        assertEquals("712115\n", revenueType.toString())
    }

    @Test
    fun `it throws exception when fewer than 6 digits`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("71211")
        }
    }

    @Test
    fun `it throws exception when more than 6 digits`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("7121150")
        }
    }

    @Test
    fun `it throws exception when non-numeric characters are provided`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("71211@")
        }
    }

    @Test
    fun `it throws exception when empty value is provided`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("")
        }
    }
}
