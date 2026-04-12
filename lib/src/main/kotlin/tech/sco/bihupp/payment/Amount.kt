package tech.sco.bihupp.payment

import kotlin.math.roundToLong

/**
 * Iznos (amount in pennies), zero-padded to 15 digits.
 *
 * The constructor accepts a raw string of digits (optionally with a decimal point, which is stripped).
 *
 * Use [of] for typed construction.
 *
 * Example: `Amount("9862")` == `Amount.of(9862)` == `Amount.of(98.62)` → "000000000009862"
 */
class Amount private constructor(
    value: String,
) : Line() {
    override val value: String = value.replace(".", "").padStart(MAX_LENGTH, '0')

    /** [amount] needs to be the amount in pennies, e.g. 9862 pennies = 98.62 BAM. */
    constructor(amount: Int) : this(amount.toString())

    /** [amount] is the BAM decimal value that will be converted from 98.62 BAM to 9862 pennies. */
    constructor(amount: Double) : this(amount.toString())

    /** [amount] is the BAM decimal value that will be converted from 98.62 BAM to 9862 pennies. */
    constructor(amount: Float) : this(amount.roundToLong().toString())

    init {
        check(this.value.length == MAX_LENGTH && this.value.all { it.isDigit() }) {
            "Amount must be a zero padded integer number (value in pennies)."
        }
        this.value.checkLengthAndChars("Amount", MAX_LENGTH, MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 15

        val EMPTY = Amount("0")

        fun of(value: String): Amount = Amount(value)

        fun of(amount: Int): Amount = of(amount.toString())

        fun of(amount: Double): Amount = of(amount.toString())

        fun of(amount: Float): Amount = of(amount.roundToLong().toString())
    }
}
