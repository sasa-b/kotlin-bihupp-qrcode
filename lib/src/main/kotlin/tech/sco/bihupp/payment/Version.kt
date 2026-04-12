package tech.sco.bihupp.payment

/**
 * BIHUPP version line.
 */
class Version : Line() {
    override val value = "BIHUPP10"

    companion object {
        const val MAX_LENGTH = 8

        val EMPTY = Version()
    }
}
