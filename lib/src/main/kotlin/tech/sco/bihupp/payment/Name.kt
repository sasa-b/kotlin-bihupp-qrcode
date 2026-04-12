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

    constructor(firstName: String, lastName: String) : this("$firstName $lastName".trim())

    companion object {
        const val MAX_LENGTH = 50

        val EMPTY = Name("")

        fun of(
            firstName: String,
            lastName: String,
        ) = Name(firstName, lastName)

        fun of(name: String) = Name(name)
    }
}
