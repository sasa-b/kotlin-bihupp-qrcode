package tech.sco.bihupp.payment

import java.time.LocalDate

// [0]=version, [1]=senderName, [2]=senderAddr1, [3]=senderAddr2, [4]=phone,
// [5]=purpose, [6]=reference, [7]=recipientName, [8]=recipientAddr1, [9]=recipientAddr2,
// [10]=senderAccount, [11]=recipientAccount, [12]=amount, [13]=currency, [14]=priority,
// [15..22]=public revenue fields
@Suppress("MagicNumber")
internal fun List<Line>.toPaymentInstruction() =
    PaymentInstruction(
        sender = toSender(),
        recipient = toRecipient(),
        purpose = getOrNull(5) as? PaymentPurpose ?: PaymentPurpose.EMPTY,
        reference = (getOrNull(6) as? PaymentReference)?.takeIf { it.value.isNotEmpty() },
        amount = getOrNull(12) as? Amount ?: Amount.EMPTY,
        paymentPriority = getOrNull(14) as? PaymentPriority ?: PaymentPriority.regular(),
        publicRevenue =
            toPublicRevenueInstruction().let {
                if (it == PublicRevenueInstruction.EMPTY) null else it
            },
    )

@Suppress("MagicNumber")
internal fun List<Line>.toRecipient() =
    Recipient(
        name = getOrNull(7) as? Name ?: Name.EMPTY,
        address =
            Address(
                addressLine1 = getOrNull(8) as? AddressLine1 ?: AddressLine1.EMPTY,
                addressLine2 = getOrNull(9) as? AddressLine2 ?: AddressLine2.EMPTY,
            ),
        account = getOrNull(11) as? RecipientAccount ?: RecipientAccount.EMPTY,
    )

@Suppress("MagicNumber")
internal fun List<Line>.toSender() =
    Sender(
        name = getOrNull(1) as? Name ?: Name.EMPTY,
        address =
            Address(
                addressLine1 = getOrNull(2) as? AddressLine1 ?: AddressLine1.EMPTY,
                addressLine2 = getOrNull(3) as? AddressLine2 ?: AddressLine2.EMPTY,
            ),
        account = getOrNull(10) as? Account ?: Account.EMPTY,
        phoneNumber = getOrNull(4) as? PhoneNumber,
    )

@Suppress("MagicNumber")
internal fun List<Line>.toPublicRevenueInstruction() =
    PublicRevenueInstruction(
        senderTaxId = getOrNull(15) as? SenderTaxId ?: SenderTaxId.EMPTY,
        paymentType = getOrNull(16) as? PaymentType ?: PaymentType.EMPTY,
        revenueType = getOrNull(17) as? RevenueType ?: RevenueType.EMPTY,
        taxPeriodStartDate = getOrNull(18) as? TaxPeriodDate ?: TaxPeriodDate.EMPTY,
        taxPeriodEndDate = getOrNull(19) as? TaxPeriodDate ?: TaxPeriodDate.EMPTY,
        municipalCode = getOrNull(20) as? MunicipalCode ?: MunicipalCode.EMPTY,
        budgetCode = getOrNull(21) as? BudgetOrgCode ?: BudgetOrgCode.EMPTY,
        paymentReference =
            getOrNull(22) as? PublicRevenueInstruction.PaymentReference
                ?: PublicRevenueInstruction.PaymentReference.EMPTY,
    )

@Suppress("LongMethod", "MagicNumber", "CyclomaticComplexMethod")
internal fun String.splitToLines(): List<Line> =
    split(Line.SEPARATOR)
        .mapIndexed { index, string ->
            when (index) {
                0 -> {
                    if (string.isEmpty()) Version.EMPTY else Version()
                }

                1 -> {
                    if (string.isEmpty()) Name.EMPTY else Name(string)
                }

                2 -> {
                    if (string.isEmpty()) AddressLine1.EMPTY else AddressLine1(string)
                }

                3 -> {
                    if (string.isEmpty()) AddressLine2.EMPTY else AddressLine2(string)
                }

                4 -> {
                    if (string.isEmpty()) EmptyLine() else PhoneNumber.of(string)
                }

                5 -> {
                    if (string.isEmpty()) PaymentPurpose.EMPTY else PaymentPurpose.of(string)
                }

                6 -> {
                    if (string.isEmpty()) PaymentReference.EMPTY else PaymentReference(string)
                }

                7 -> {
                    if (string.isEmpty()) Name.EMPTY else Name(string)
                }

                8 -> {
                    if (string.isEmpty()) AddressLine1.EMPTY else AddressLine1(string)
                }

                9 -> {
                    if (string.isEmpty()) AddressLine2.EMPTY else AddressLine2(string)
                }

                10 -> {
                    if (string.isEmpty()) Account.EMPTY else Account(string)
                }

                11 -> {
                    if (string.isEmpty()) {
                        RecipientAccount.EMPTY
                    } else {
                        RecipientAccount.of(string.split(",").map { Account(it) })
                    }
                }

                12 -> {
                    if (string.isEmpty()) Amount.EMPTY else Amount.of(string)
                }

                13 -> {
                    if (string.isEmpty()) Currency.EMPTY else Currency()
                }

                14 -> {
                    if (string == "D") PaymentPriority.urgent() else PaymentPriority.regular()
                }

                15 -> {
                    if (string.isEmpty()) SenderTaxId.EMPTY else SenderTaxId(string)
                }

                16 -> {
                    if (string.isEmpty()) PaymentType.EMPTY else PaymentType(string)
                }

                17 -> {
                    if (string.isEmpty()) RevenueType.EMPTY else RevenueType(string)
                }

                18 -> {
                    if (string.isEmpty()) TaxPeriodDate.EMPTY else TaxPeriodDate.of(LocalDate.parse(string))
                }

                19 -> {
                    if (string.isEmpty()) TaxPeriodDate.EMPTY else TaxPeriodDate.of(LocalDate.parse(string))
                }

                20 -> {
                    if (string.isEmpty()) MunicipalCode.EMPTY else MunicipalCode(string)
                }

                21 -> {
                    if (string.isEmpty()) BudgetOrgCode.EMPTY else BudgetOrgCode(string)
                }

                22 -> {
                    if (string.isEmpty()) PaymentReference.EMPTY else PublicRevenueInstruction.PaymentReference(string)
                }

                else -> {
                    EmptyLine()
                }
            }
        }
