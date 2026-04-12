package tech.sco.bihupp.payment

class Currency : Line() {
    override val value = "BAM"

    companion object {
        const val MAX_LENGTH = 3
    }
}
