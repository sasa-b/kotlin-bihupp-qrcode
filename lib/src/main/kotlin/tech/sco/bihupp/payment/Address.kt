package tech.sco.bihupp.payment

data class Address(
    val addressLine1: AddressLine1,
    val addressLine2: AddressLine2,
)

/**
 * Adresa (ulica i broj).
 * */
class AddressLine1 private constructor(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("AddressLine1", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 50

        fun of(
            street: String,
            number: String,
        ): AddressLine1 =
            AddressLine1(
                if (number.isBlank()) street.trim() else "$street $number".trim(),
            )
    }
}

/**
 * Adresa (poštanski broj i mjesto).
 * */
class AddressLine2 private constructor(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("AddressLine2", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 25

        fun of(
            postcode: String,
            town: String,
        ) = AddressLine2("$postcode $town".trim())
    }
}
