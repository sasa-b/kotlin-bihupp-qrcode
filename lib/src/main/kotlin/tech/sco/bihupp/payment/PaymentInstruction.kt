package tech.sco.bihupp.payment

import qrcode.QRCode
import tech.sco.bihupp.of

/**
 * Represents a BIHUPP payment instruction text payload.
 *
 * Call [toString] to get the newline-delimited string that is encoded into a QR code.
 * The [lines] method returns the same data as a structured list of lines.
 *
 */
data class PaymentInstruction(
    val recipient: Recipient,
    val purpose: PaymentPurpose,
    val amount: Amount,
    val sender: Sender? = null,
    val reference: PaymentReference? = null,
    val paymentPriority: PaymentPriority = PaymentPriority.regular(),
    val publicRevenue: PublicRevenueInstruction? = null,
) {
    /** Returns the ordered lines of the payload. The order must not change. */
    fun lines(): List<Line> =
        buildList {
            add(Version())
            add(sender?.name ?: EmptyLine())
            add(sender?.address?.addressLine1 ?: EmptyLine())
            add(sender?.address?.addressLine2 ?: EmptyLine())
            add(sender?.phoneNumber ?: EmptyLine())
            add(purpose)
            add(reference ?: EmptyLine())
            add(recipient.name)
            add(recipient.address.addressLine1)
            add(recipient.address.addressLine2)
            add(sender?.account ?: EmptyLine())
            add(recipient.account)
            add(amount)
            add(Currency())
            add(paymentPriority)
            add(publicRevenue?.senderTaxId ?: EmptyLine())
            add(publicRevenue?.paymentType ?: EmptyLine())
            add(publicRevenue?.revenueType ?: EmptyLine())
            add(publicRevenue?.taxPeriodStartDate ?: EmptyLine())
            add(publicRevenue?.taxPeriodEndDate ?: EmptyLine())
            add(publicRevenue?.municipalCode ?: EmptyLine())
            add(publicRevenue?.budgetCode ?: EmptyLine())
            add(publicRevenue?.paymentReference ?: EmptyLine())
        }

    override fun toString(): String = lines().joinToString("")

    fun toQRCode() = QRCode.of(this)
}

/**
 * Svrha uplate (payment purpose / description).
 *
 * Newlines are normalized to spaces.
 * */
class PaymentPurpose private constructor(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("PaymentPurpose", MAX_LENGTH)
    }

    companion object {
        const val MAX_LENGTH = 110

        fun of(value: String): PaymentPurpose = PaymentPurpose(value.replace("\n", " "))
    }
}

/** Poziv na broj / referenca (payment reference). */
data class PaymentReference(
    override val value: String,
) : Line() {
    init {
        value.checkLengthAndChars("PaymentReference", MAX_LENGTH)
    }

    override fun toString() = super.toString()

    companion object {
        const val MAX_LENGTH = 30

        fun of(value: String): PaymentReference = PaymentReference(value)
    }
}

enum class Priority(
    val value: String,
) {
    Regular("N"),
    Urgent("D"),
}

data class PaymentPriority(
    private val priority: Priority = Priority.Regular,
) : Line() {
    override val value: String = priority.value

    override fun toString() = super.toString()

    companion object {
        const val MAX_LENGTH = 1

        fun regular() = PaymentPriority(Priority.Regular)

        fun urgent() = PaymentPriority(Priority.Urgent)
    }
}
