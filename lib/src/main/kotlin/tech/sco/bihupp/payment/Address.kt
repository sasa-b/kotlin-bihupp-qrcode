package tech.sco.bihupp.payment

data class Address(
    val addressLine1: AddressLine1,
    val addressLine2: AddressLine2,
)

/**
 * Adresa (ulica i broj).
 * */
data class AddressLine1(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("AddressLine1", MAX_LENGTH)
    }

    constructor(
        street: String,
        number: String,
    ) : this(
        if (number.isBlank()) street.trim() else "$street $number".trim(),
    )

    companion object {
        const val MAX_LENGTH = 50

        val EMPTY = AddressLine1("")

        fun of(
            street: String,
            number: String,
        ): AddressLine1 = AddressLine1(street, number)
    }
}

/**
 * Adresa (poštanski broj i mjesto).
 * */
class AddressLine2(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("AddressLine2", MAX_LENGTH)
    }

    constructor(postcode: String, town: String) : this("$postcode $town".trim())

    companion object {
        const val MAX_LENGTH = 25

        val EMPTY = AddressLine2("")

        fun of(
            postcode: String,
            town: String,
        ) = AddressLine2(postcode, town)
    }
}
