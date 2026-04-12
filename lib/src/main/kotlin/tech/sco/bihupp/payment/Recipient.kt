package tech.sco.bihupp.payment

/** Primalac (payment recipient). */
data class Recipient(
    val name: Name,
    val address: Address,
    val account: RecipientAccount,
)

/**
 * Račun primaoca (recipient account).
 *
 * Accepts 1–20 [Account] values, stored as a comma-separated string.
 * The combined string must not exceed 339 characters.
 */
class RecipientAccount private constructor(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("RecipientAccount", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 339
        const val MAX_ACCOUNTS = 20

        fun of(vararg accounts: Account): RecipientAccount {
            check(accounts.isNotEmpty()) {
                "At least one account is required."
            }

            check(accounts.size <= MAX_ACCOUNTS) {
                "Maximum of $MAX_ACCOUNTS accounts allowed, got ${accounts.size}."
            }

            return RecipientAccount(accounts.joinToString(",") { it.value })
        }

        fun of(accounts: List<Account>): RecipientAccount = of(*accounts.toTypedArray())
    }
}
