package tech.sco.bihupp.payment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NameTest {
    @Test
    fun `creates individual name`() {
        val name = Name.of("Marko", "Marković")

        assertEquals("Marko Marković", name.value)
    }

    @Test
    fun `creates business name`() {
        val name = Name.of("ACME Corporation")

        assertEquals("ACME Corporation", name.value)
    }

    @Test
    fun `converts to string that ends with newline`() {
        val name = Name.of("Marko", "Marković")

        assertEquals("Marko Marković\n", name.toString())
    }

    @Test
    fun `throws when value exceeds max length`() {
        // "a" * 30 + " " + "b" * 30 = 61 chars, exceeds limit of 50
        assertFailsWith<IllegalStateException> {
            Name.of("a".repeat(30), "b".repeat(30))
        }
    }

    @Test
    fun `accepts name exactly at max length`() {
        // "a" * 24 + " " + "b" * 25 = 50 chars
        val name = Name.of("a".repeat(24), "b".repeat(25))

        assertEquals(Name.MAX_LENGTH, name.value.length)
    }

    @Test
    fun `throws when value contains invalid characters`() {
        assertFailsWith<IllegalStateException> {
            Name.of("Invalid@Name", "Test")
        }
    }
}
