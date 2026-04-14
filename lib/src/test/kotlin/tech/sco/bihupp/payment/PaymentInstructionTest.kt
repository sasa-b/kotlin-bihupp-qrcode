package tech.sco.bihupp.payment

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class PaymentInstructionTest {
    @Test
    fun `it creates with valid payment instruction`() {
        val instruction =
            PaymentInstruction(
                sender = sender(),
                recipient = recipient(),
                purpose = PaymentPurpose.of("Invoice payment"),
                reference = PaymentReference("INV2024001"),
                amount = Amount.of("10000"),
            )

        assertEquals("Invoice payment", instruction.purpose.value)
        assertEquals("000000000010000", instruction.amount.value)
        assertEquals("BAM", Currency().value)
        assertEquals("N", instruction.paymentPriority.value)
        assertNull(instruction.publicRevenue)
    }

    @Test
    fun `it converts to string with all lines in correct order`() {
        val instruction =
            PaymentInstruction(
                sender = sender(),
                recipient = recipient(),
                purpose = PaymentPurpose.of("Invoice payment"),
                reference = PaymentReference("INV2024001"),
                amount = Amount.of("10000"),
            )

        val expected =
            listOf(
                "BIHUPP10\n", // version
                "Marko Marković\n", // sender name
                "Ulica Meše Selimovića 12\n", // sender address line 1
                "78000 Banja Luka\n", // sender address line 2
                "\n", // sender phone (empty)
                "Invoice payment\n", // purpose
                "INV2024001\n", // reference
                "Pero Perić\n", // recipient name
                "Titova 1\n", // recipient address line 1
                "71000 Sarajevo\n", // recipient address line 2
                "1234567890123456\n", // sender account
                "9876543210987654\n", // recipient account
                "000000000010000\n", // amount
                "BAM\n", // currency
                "N\n", // payment priority
                "\n", // sender tax id (empty)
                "\n", // payment type (empty)
                "\n", // revenue type (empty)
                "\n", // tax period start date (empty)
                "\n", // tax period end date (empty)
                "\n", // municipal code (empty)
                "\n", // budget org code (empty)
                "\n", // public revenue payment reference (empty)
            ).joinToString("")

        assertEquals(expected, instruction.toString())
    }

    @Test
    fun `it converts public revenue instruction to string with all lines in correct order`() {
        val instruction =
            PaymentInstruction(
                sender = sender(Name.of("Example Company d.o.o.")),
                recipient = recipient(Name("Trezor")),
                purpose = PaymentPurpose.of("Tax payment"),
                reference = null,
                amount = Amount.of("500000"),
                publicRevenue =
                    PublicRevenueInstruction(
                        senderTaxId = SenderTaxId("0101990123456"),
                        paymentType = PaymentType("3"),
                        revenueType = RevenueType("712115"),
                        taxPeriodStartDate = TaxPeriodDate.of(LocalDate.of(2024, 1, 1)),
                        taxPeriodEndDate = TaxPeriodDate.of(LocalDate.of(2024, 12, 31)),
                        municipalCode = MunicipalCode("077"),
                        budgetCode = BudgetOrgCode("1200200"),
                        paymentReference = PublicRevenueInstruction.PaymentReference("7110578163"),
                    ),
            )

        val expected =
            listOf(
                "BIHUPP10\n", // version
                "Example Company d.o.o.\n", // sender name
                "Ulica Meše Selimovića 12\n", // sender address line 1
                "78000 Banja Luka\n", // sender address line 2
                "\n", // sender phone (empty)
                "Tax payment\n", // purpose
                "\n", // reference (empty)
                "Trezor\n", // recipient name
                "Titova 1\n", // recipient address line 1
                "71000 Sarajevo\n", // recipient address line 2
                "1234567890123456\n", // sender account
                "9876543210987654\n", // recipient account
                "000000000500000\n", // amount
                "BAM\n", // currency
                "N\n", // payment priority
                "0101990123456\n", // sender tax id
                "3\n", // payment type
                "712115\n", // revenue type
                "01012024\n", // tax period start date
                "31122024\n", // tax period end date
                "077\n", // municipal code
                "1200200\n", // budget org code
                "7110578163\n", // public revenue payment reference
            ).joinToString("")

        assertEquals(expected, instruction.toString())
    }

    @Test
    fun `it replaces reference with empty line when not provided`() {
        val instruction =
            PaymentInstruction(
                sender = sender(),
                recipient = recipient(),
                purpose = PaymentPurpose.of("Invoice payment"),
                reference = null,
                amount = Amount.of("10000"),
            )

        // reference is at index 6: version, sender name, addr1, addr2, phone, purpose, reference
        assertIs<EmptyLine>(instruction.lines()[6])
    }

    @Test
    fun `it converts water bill example with sender to expected string`() {
        val instruction =
            PaymentInstruction(
                sender =
                    Sender(
                        name = Name.of("DENISA", "KOVAČEVIĆ-BATIĆ"),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of("ŠARAJEVSKA ULICA", "43"),
                                addressLine2 = AddressLine2.of("78000", "BANJA LUKA"),
                            ),
                        account = Account("1995320021237616"),
                    ),
                recipient =
                    Recipient(
                        name = Name.of("VODOVOD MOSTAR"),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of("ALEKSE ŠANTIĆA", ""),
                                addressLine2 = AddressLine2.of("88000", "MOSTAR"),
                            ),
                        account = RecipientAccount.of(Account("1010000236542719")),
                    ),
                purpose = PaymentPurpose.of("Troškovi vode za 6. mjesec"),
                reference = PaymentReference("1445-26554-11222"),
                amount = Amount.of("9862"),
                paymentPriority = PaymentPriority.urgent(),
            )

        val expected =
            listOf(
                "BIHUPP10\n", // version
                "DENISA KOVAČEVIĆ-BATIĆ\n", // sender name
                "ŠARAJEVSKA ULICA 43\n", // sender address line 1
                "78000 BANJA LUKA\n", // sender address line 2
                "\n", // sender phone (empty)
                "Troškovi vode za 6. mjesec\n", // purpose
                "1445-26554-11222\n", // reference
                "VODOVOD MOSTAR\n", // recipient name
                "ALEKSE ŠANTIĆA\n", // recipient address line 1
                "88000 MOSTAR\n", // recipient address line 2
                "1995320021237616\n", // sender account
                "1010000236542719\n", // recipient account
                "000000000009862\n", // amount
                "BAM\n", // currency
                "D\n", // payment priority (urgent)
                "\n", // sender tax id (empty)
                "\n", // payment type (empty)
                "\n", // revenue type (empty)
                "\n", // tax period start date (empty)
                "\n", // tax period end date (empty)
                "\n", // municipal code (empty)
                "\n", // budget org code (empty)
                "\n", // public revenue payment reference (empty)
            ).joinToString("")

        assertEquals(expected, instruction.toString())
    }

    @Test
    fun `it converts water bill example without sender to expected string`() {
        // Sender fields are empty — the bank auto-populates them from the logged-in user's session
        val instruction =
            PaymentInstruction(
                sender =
                    Sender(
                        name = Name(""),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of("", ""),
                                addressLine2 = AddressLine2.of("", ""),
                            ),
                        account = Account("1995320021237616"),
                    ),
                recipient =
                    Recipient(
                        name = Name.of("VODOVOD MOSTAR"),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of("ALEKSE ŠANTIĆA", ""),
                                addressLine2 = AddressLine2.of("88000", "MOSTAR"),
                            ),
                        account = RecipientAccount.of(Account("1010000236542719")),
                    ),
                purpose = PaymentPurpose.of("Troškovi vode za 6. mjesec"),
                reference = PaymentReference("1445-26554-11222"),
                amount = Amount.of("9862"),
                paymentPriority = PaymentPriority.urgent(),
            )

        val expected =
            listOf(
                "BIHUPP10\n", // version
                "\n", // sender name (empty — bank fills from logged-in user)
                "\n", // sender address line 1 (empty)
                "\n", // sender address line 2 (empty)
                "\n", // sender phone (empty)
                "Troškovi vode za 6. mjesec\n", // purpose
                "1445-26554-11222\n", // reference
                "VODOVOD MOSTAR\n", // recipient name
                "ALEKSE ŠANTIĆA\n", // recipient address line 1
                "88000 MOSTAR\n", // recipient address line 2
                "1995320021237616\n", // sender account
                "1010000236542719\n", // recipient account
                "000000000009862\n", // amount
                "BAM\n", // currency
                "D\n", // payment priority (urgent)
                "\n", // sender tax id (empty)
                "\n", // payment type (empty)
                "\n", // revenue type (empty)
                "\n", // tax period start date (empty)
                "\n", // tax period end date (empty)
                "\n", // municipal code (empty)
                "\n", // budget org code (empty)
                "\n", // public revenue payment reference (empty)
            ).joinToString("")

        assertEquals(expected, instruction.toString())
    }

    @Test
    fun `it converts max field length example with utf8 characters to expected string`() {
        val maxName = "Aa".repeat(25) // 50 chars
        val maxAddr1 = "Aa".repeat(25) // 50 chars
        val maxAddr2 = "Aa".repeat(12) + "A" // 25 chars
        val maxPurpose = "Aa".repeat(55) // 110 chars
        val maxReference = "Aa".repeat(15) // 30 chars
        val recipientAccount = List(20) { "1234567890123456" }.joinToString(",") // 339 chars

        val instruction =
            PaymentInstruction(
                sender =
                    Sender(
                        name = Name(maxName),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of(maxAddr1, ""),
                                addressLine2 = AddressLine2.of(maxAddr2, ""),
                            ),
                        account = Account("1234567890123456"),
                        phoneNumber = PhoneNumber.of("+12345678901234"),
                    ),
                recipient =
                    Recipient(
                        name = Name.of(maxName),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of(maxAddr1, ""),
                                addressLine2 = AddressLine2.of(maxAddr2, ""),
                            ),
                        account = RecipientAccount.of(List(20) { Account("1234567890123456") }),
                    ),
                purpose = PaymentPurpose.of(maxPurpose),
                reference = PaymentReference(maxReference),
                amount = Amount.of("123456789012345"),
                publicRevenue =
                    PublicRevenueInstruction(
                        senderTaxId = SenderTaxId("1234567890123"),
                        paymentType = PaymentType("0"),
                        revenueType = RevenueType("123456"),
                        taxPeriodStartDate = TaxPeriodDate.of(LocalDate.of(2023, 5, 12)),
                        taxPeriodEndDate = TaxPeriodDate.of(LocalDate.of(2024, 5, 12)),
                        municipalCode = MunicipalCode("123"),
                        budgetCode = BudgetOrgCode("1234567"),
                        paymentReference = PublicRevenueInstruction.PaymentReference("1234567890"),
                    ),
            )

        val expected =
            listOf(
                "BIHUPP10\n", // version
                "$maxName\n", // sender name (50 chars)
                "$maxAddr1\n", // sender address line 1 (50 chars)
                "$maxAddr2\n", // sender address line 2 (25 chars)
                "+12345678901234\n", // sender phone (15 chars)
                "$maxPurpose\n", // purpose (110 chars)
                "$maxReference\n", // reference (30 chars)
                "$maxName\n", // recipient name (50 chars)
                "$maxAddr1\n", // recipient address line 1 (50 chars)
                "$maxAddr2\n", // recipient address line 2 (25 chars)
                "1234567890123456\n", // sender account (16 chars)
                "$recipientAccount\n", // recipient account (20 × 16 + 19 commas = 339 chars)
                "123456789012345\n", // amount (15 chars)
                "BAM\n", // currency
                "N\n", // payment priority
                "1234567890123\n", // sender tax id (13 chars)
                "0\n", // payment type (1 char)
                "123456\n", // revenue type (6 chars)
                "12052023\n", // tax period start date (ddMMyyyy of 2023-05-12)
                "12052024\n", // tax period end date (ddMMyyyy of 2024-05-12)
                "123\n", // municipal code (3 chars)
                "1234567\n", // budget org code (7 chars)
                "1234567890\n", // public revenue payment reference (10 digits)
            ).joinToString("")

        assertEquals(expected, instruction.toString())
    }

    @Test
    fun `it converts max field length example with ascii characters to expected string`() {
        val maxName = "Aa".repeat(25) // 50 chars
        val maxAddr1 = "Aa".repeat(25) // 50 chars
        val maxAddr2 = "Aa".repeat(12) + "A" // 25 chars
        val maxPurpose = "Aa".repeat(55) // 110 chars
        val maxReference = "Aa".repeat(15) // 30 chars
        val recipientAccount = List(20) { "1234567890123456" }.joinToString(",") // 339 chars

        val instruction =
            PaymentInstruction(
                sender =
                    Sender(
                        name = Name(maxName),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of(maxAddr1, ""),
                                addressLine2 = AddressLine2.of(maxAddr2, ""),
                            ),
                        account = Account("1234567890123456"),
                        phoneNumber = PhoneNumber.of("+12345678901234"),
                    ),
                recipient =
                    Recipient(
                        name = Name.of(maxName),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of(maxAddr1, ""),
                                addressLine2 = AddressLine2.of(maxAddr2, ""),
                            ),
                        account = RecipientAccount.of(List(20) { Account("1234567890123456") }),
                    ),
                purpose = PaymentPurpose.of(maxPurpose),
                reference = PaymentReference(maxReference),
                amount = Amount.of("123456789012345"),
                publicRevenue =
                    PublicRevenueInstruction(
                        senderTaxId = SenderTaxId("1234567890123"),
                        paymentType = PaymentType("0"),
                        revenueType = RevenueType("123456"),
                        taxPeriodStartDate = TaxPeriodDate.of(LocalDate.of(2045, 6, 1)),
                        taxPeriodEndDate = TaxPeriodDate.of(LocalDate.of(2099, 12, 31)),
                        municipalCode = MunicipalCode("123"),
                        budgetCode = BudgetOrgCode("1234567"),
                        paymentReference = PublicRevenueInstruction.PaymentReference("0987654321"),
                    ),
            )

        val expected =
            listOf(
                "BIHUPP10\n", // version
                "$maxName\n", // sender name (50 chars)
                "$maxAddr1\n", // sender address line 1 (50 chars)
                "$maxAddr2\n", // sender address line 2 (25 chars)
                "+12345678901234\n", // sender phone (15 chars, E.164)
                "$maxPurpose\n", // purpose (110 chars)
                "$maxReference\n", // reference (30 chars)
                "$maxName\n", // recipient name (50 chars)
                "$maxAddr1\n", // recipient address line 1 (50 chars)
                "$maxAddr2\n", // recipient address line 2 (25 chars)
                "1234567890123456\n", // sender account (16 chars)
                "$recipientAccount\n", // recipient account (20 × 16 + 19 commas = 339 chars)
                "123456789012345\n", // amount (15 chars)
                "BAM\n", // currency
                "N\n", // payment priority
                "1234567890123\n", // sender tax id (13 chars)
                "0\n", // payment type (1 char)
                "123456\n", // revenue type (6 chars)
                "01062045\n", // tax period start date (ddMMyyyy of 2045-06-01)
                "31122099\n", // tax period end date (ddMMyyyy of 2099-12-31)
                "123\n", // municipal code (3 chars)
                "1234567\n", // budget org code (7 chars)
                "0987654321\n", // public revenue payment reference (10 digits)
            ).joinToString("")

        assertEquals(expected, instruction.toString())
    }
}

private fun sender(name: Name = Name.of("Marko", "Marković")) =
    Sender(
        name = name,
        address =
            Address(
                addressLine1 = AddressLine1.of("Ulica Meše Selimovića", "12"),
                addressLine2 = AddressLine2.of("78000", "Banja Luka"),
            ),
        account = Account("1234567890123456"),
    )

private fun recipient(name: Name = Name.of("Pero", "Perić")) =
    Recipient(
        name = name,
        address =
            Address(
                addressLine1 = AddressLine1.of("Titova", "1"),
                addressLine2 = AddressLine2.of("71000", "Sarajevo"),
            ),
        account = RecipientAccount.of(Account("9876543210987654")),
    )
