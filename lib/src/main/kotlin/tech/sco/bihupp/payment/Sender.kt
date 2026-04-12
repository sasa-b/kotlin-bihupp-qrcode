package tech.sco.bihupp.payment

/**
 * Pošiljaoc (payment sender).
 * */
data class Sender(
    val name: Name,
    val address: Address,
    val account: Account,
    val phoneNumber: PhoneNumber? = null,
)

/**
 * Broj telefona uplatioca/primaoca in E.164 format.
 *
 * An E.164 number has three components:
 *  - The prefix "+".
 *  - A 1–3 digit country code.
 *  - A subscriber number.
 *
 * Spaces are stripped automatically. Max 15 characters.
 */
class PhoneNumber private constructor(
    override val value: String,
) : Line() {
    init {
        check(value.startsWith("+")) {
            "Phone number must be in E.164 format starting with +."
        }
        value.checkLengthAndChars("PhoneNumber", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 15

        fun of(value: String): PhoneNumber = PhoneNumber(value.replace(" ", ""))
    }
}
