package tech.sco.bihupp.payment

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** Uplata javnih prihoda */
data class PublicRevenueInstruction(
    val senderTaxId: SenderTaxId,
    val paymentType: PaymentType,
    val revenueType: RevenueType,
    val taxPeriodStartDate: TaxPeriodDate,
    val taxPeriodEndDate: TaxPeriodDate,
    val municipalCode: MunicipalCode,
    val budgetCode: BudgetOrgCode,
    val paymentReference: PaymentReference,
) {
    /** Poziv na broj. */
    data class PaymentReference(
        override val value: String,
    ) : Line() {
        init {
            check(value.length == MAX_LENGTH && value.all { it.isDigit() }) {
                "Payment reference must be a 10 digit integer, got: $value."
            }
        }

        override fun toString() = super.toString()

        companion object {
            const val MAX_LENGTH = 10

            val EMPTY = PaymentReference("0".padStart(MAX_LENGTH, '0'))

            fun of(value: String): PaymentReference = PaymentReference(value)
        }
    }

    companion object {
        val EMPTY =
            PublicRevenueInstruction(
                senderTaxId = SenderTaxId.EMPTY,
                paymentType = PaymentType.EMPTY,
                revenueType = RevenueType.EMPTY,
                taxPeriodStartDate = TaxPeriodDate.EMPTY,
                taxPeriodEndDate = TaxPeriodDate.EMPTY,
                municipalCode = MunicipalCode.EMPTY,
                budgetCode = BudgetOrgCode.EMPTY,
                paymentReference = PaymentReference.EMPTY,
            )
    }
}

/** JMBG */
class SenderTaxId(
    override val value: String,
) : Line() {
    init {
        check(value.length == MAX_LENGTH && value.all { it.isDigit() }) {
            "SenderTaxId must be a 13 digit integer, got: $value."
        }
    }

    companion object {
        const val MAX_LENGTH = 13

        val EMPTY = SenderTaxId("0".padStart(MAX_LENGTH, '0'))
    }
}

/** Single digit 0–9 identifying the public payment type (vrsta uplate). */
class PaymentType(
    override val value: String,
) : Line() {
    init {
        check(value.length == MAX_LENGTH && value.all { it.isDigit() }) {
            "PaymentType must be a one digit number, got: $value."
        }
    }

    companion object {
        const val MAX_LENGTH = 1

        val EMPTY = PaymentType("0".padStart(MAX_LENGTH, '0'))
    }
}

/** Exactly 6 digits identifying the revenue type (vrsta prihoda). */
class RevenueType(
    override val value: String,
) : Line() {
    init {
        check(value.length == MAX_LENGTH && value.all { it.isDigit() }) {
            "RevenueType must be 6 digits, got: $value."
        }
    }

    companion object {
        const val MAX_LENGTH = 6

        val EMPTY = RevenueType("0".padStart(MAX_LENGTH, '0'))
    }
}

/**
 * Tax period date (period oporezivanja) in ddMMyyyy format.
 * Use [of] to construct from a [LocalDate].
 */
class TaxPeriodDate private constructor(
    override val value: String,
) : Line() {
    constructor(date: LocalDate) : this(
        date.format(FORMATTER),
    )

    companion object {
        const val MAX_LENGTH = 8

        val EMPTY = TaxPeriodDate("")

        private val FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy")

        fun of(date: LocalDate): TaxPeriodDate = TaxPeriodDate(date)
    }
}

/** Exactly 3 digits – municipality (opština) code. */
class MunicipalCode(
    override val value: String,
) : Line() {
    init {
        check(value.length == MAX_LENGTH && value.all { it.isDigit() }) {
            "MunicipalCode must be a 3 digit integer, got: $value."
        }
    }

    companion object {
        const val MAX_LENGTH = 3

        val EMPTY = MunicipalCode("0".padStart(MAX_LENGTH, '0'))

        fun of(value: String): MunicipalCode = MunicipalCode(value)
    }
}

/** Budzetska organizacija. */
class BudgetOrgCode(
    override val value: String,
) : Line() {
    init {
        check(value.length == MAX_LENGTH && value.all { it.isDigit() }) {
            "BudgetOrgCode must be a 7 digit integer, got: $value"
        }
    }

    companion object {
        const val MAX_LENGTH = 7

        val EMPTY = BudgetOrgCode("0".padStart(MAX_LENGTH, '0'))

        fun of(value: String): BudgetOrgCode = BudgetOrgCode(value)
    }
}
