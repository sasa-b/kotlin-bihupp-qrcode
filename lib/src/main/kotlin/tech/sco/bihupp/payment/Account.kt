package tech.sco.bihupp.payment

/**
 * Račun pošiljaoca (sender's account number).
 * */
class Account(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("Account", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 16

        val EMPTY = Account("")

        fun of(value: String): Account = Account(value)
    }
}
