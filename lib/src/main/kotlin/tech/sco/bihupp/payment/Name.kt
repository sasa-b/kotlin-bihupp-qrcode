package tech.sco.bihupp.payment

/**
 * Naziv/Ime i prezime (uplatioca ili primaoca)
 * */
class Name(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("Name", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 50

        fun of(
            firstName: String,
            lastName: String,
        ) = Name("$firstName $lastName".trim())

        fun business(name: String) = Name(name)
    }
}
