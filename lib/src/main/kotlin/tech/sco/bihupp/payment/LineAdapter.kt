package tech.sco.bihupp.payment

import java.time.LocalDate

internal fun List<Line>.toPaymentInstruction() =
    PaymentInstruction(
        recipient = toRecipient(),
        purpose = toPurpose(),
        amount = toAmount(),
        sender = toSender(),
        reference = toPaymentReference(),
        paymentPriority = toPaymentPriority(),
        publicRevenue = toPublicRevenueInstruction(),
    )

internal fun List<Line>.toRecipient() =
    Recipient(
        name = (find { it is Name } as? Name) ?: Name.EMPTY,
        address =
            Address(
                addressLine1 = (find { it is AddressLine1 } as? AddressLine1) ?: AddressLine1.EMPTY,
                addressLine2 = (find { it is AddressLine2 } as? AddressLine2) ?: AddressLine2.EMPTY,
            ),
        account = (find { it is RecipientAccount } as? RecipientAccount) ?: RecipientAccount.EMPTY,
    )

internal fun List<Line>.toSender() =
    Sender(
        name = (find { it is Name } as? Name) ?: Name(""),
        address =
            Address(
                addressLine1 = (find { it is AddressLine1 } as? AddressLine1) ?: AddressLine1.EMPTY,
                addressLine2 = (find { it is AddressLine2 } as? AddressLine2) ?: AddressLine2.EMPTY,
            ),
        account = (find { it is Account } as? Account) ?: Account.EMPTY,
        phoneNumber = find { it is PhoneNumber } as? PhoneNumber,
    )

internal fun List<Line>.toPaymentPriority() = (find { it is PaymentPriority } as? PaymentPriority) ?: PaymentPriority.regular()

internal fun List<Line>.toPaymentReference() = (find { it is PaymentReference } as? PaymentReference) ?: PaymentReference.EMPTY

internal fun List<Line>.toPurpose() = (find { it is PaymentPurpose } as? PaymentPurpose) ?: PaymentPurpose.EMPTY

internal fun List<Line>.toAmount() = (find { it is Amount } as? Amount) ?: Amount.EMPTY

internal fun List<Line>.toPublicRevenueInstruction() =
    PublicRevenueInstruction(
        senderTaxId = (find { it is SenderTaxId } as? SenderTaxId) ?: SenderTaxId.EMPTY,
        paymentType = (find { it is PaymentType } as? PaymentType) ?: PaymentType.EMPTY,
        revenueType = (find { it is RevenueType } as? RevenueType) ?: RevenueType.EMPTY,
        taxPeriodStartDate = (find { it is TaxPeriodDate } as? TaxPeriodDate) ?: TaxPeriodDate.EMPTY,
        taxPeriodEndDate = (find { it is TaxPeriodDate } as? TaxPeriodDate) ?: TaxPeriodDate.EMPTY,
        municipalCode = (find { it is MunicipalCode } as? MunicipalCode) ?: MunicipalCode.EMPTY,
        budgetCode = (find { it is BudgetOrgCode } as? BudgetOrgCode) ?: BudgetOrgCode.EMPTY,
        paymentReference =
            (find { it is PublicRevenueInstruction.PaymentReference } as? PublicRevenueInstruction.PaymentReference)
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
                    if (string.isEmpty()) EmptyLine() else Name(string)
                }

                2 -> {
                    if (string.isEmpty()) AddressLine1.EMPTY else AddressLine1.of(string, "")
                }

                3 -> {
                    if (string.isEmpty()) AddressLine2.EMPTY else AddressLine2.of(string, "")
                }

                4 -> {
                    if (string.isEmpty()) PhoneNumber.EMPTY else PhoneNumber.of(string)
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
        }.filterNot { it is EmptyLine }
