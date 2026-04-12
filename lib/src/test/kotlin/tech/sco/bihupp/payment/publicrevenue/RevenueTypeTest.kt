package tech.sco.bihupp.payment.publicrevenue

import tech.sco.bihupp.payment.RevenueType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RevenueTypeTest {
    @Test
    fun `creates with valid revenue type`() {
        val revenueType = RevenueType("712115")

        assertEquals("712115", revenueType.value)
    }

    @Test
    fun `converts to string that ends with lf char`() {
        val revenueType = RevenueType("712115")

        assertEquals("712115\n", revenueType.toString())
    }

    @Test
    fun `throws exception when fewer than 6 digits`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("71211")
        }
    }

    @Test
    fun `throws exception when more than 6 digits`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("7121150")
        }
    }

    @Test
    fun `throws exception when non-numeric characters are provided`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("71211@")
        }
    }

    @Test
    fun `throws exception when empty value is provided`() {
        assertFailsWith<IllegalStateException> {
            RevenueType("")
        }
    }
}
