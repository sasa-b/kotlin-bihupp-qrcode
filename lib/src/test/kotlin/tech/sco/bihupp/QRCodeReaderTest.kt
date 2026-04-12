package tech.sco.bihupp

import tech.sco.bihupp.payment.Account
import tech.sco.bihupp.payment.Address
import tech.sco.bihupp.payment.AddressLine1
import tech.sco.bihupp.payment.AddressLine2
import tech.sco.bihupp.payment.Amount
import tech.sco.bihupp.payment.Name
import tech.sco.bihupp.payment.PaymentInstruction
import tech.sco.bihupp.payment.PaymentPriority
import tech.sco.bihupp.payment.PaymentPurpose
import tech.sco.bihupp.payment.PaymentReference
import tech.sco.bihupp.payment.PhoneNumber
import tech.sco.bihupp.payment.Recipient
import tech.sco.bihupp.payment.RecipientAccount
import tech.sco.bihupp.payment.Sender
import tech.sco.bihupp.qrcode.QRCodePixels
import tech.sco.bihupp.qrcode.QRCodeReader
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertEquals

class QRCodeReaderTest {
    @Test
    fun `scans example png and decodes it to the expected payment instruction`() {
        val image = ImageIO.read(javaClass.getResourceAsStream("/example.png"))
        val pixels =
            QRCodePixels(
                width = image.width,
                height = image.height,
                argbPixels = image.getRGB(0, 0, image.width, image.height, null, 0, image.width),
            )

        val parsed = parsePaymentInstruction(QRCodeReader.scan(pixels)!!)

        val expected =
            PaymentInstruction(
                sender =
                    Sender(
                        name = Name.of("Marko", "Marković"),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of("Ulica Meše Selimovića", "12"),
                                addressLine2 = AddressLine2.of("78000", "Banja Luka"),
                            ),
                        account = Account("1234567890123456"),
                    ),
                recipient =
                    Recipient(
                        name = Name.of("Pero", "Perić"),
                        address =
                            Address(
                                addressLine1 = AddressLine1.of("Titova", "1"),
                                addressLine2 = AddressLine2.of("71000", "Sarajevo"),
                            ),
                        account = RecipientAccount.of(Account("9876543210987654")),
                    ),
                purpose = PaymentPurpose.of("Račun za el. energiju"),
                reference = PaymentReference("2024001"),
                amount = Amount.of("10000"),
            )

        assertEquals(expected.toString(), parsed.toString())
    }
}

/**
 * Parses a BIHUPP QR code payload string back into a [PaymentInstruction].
 *
 * Each line in the payload is `$value\n`, so splitting by `\n` yields field values at fixed indices:
 * [0] version, [1] sender name, [2] sender addr1, [3] sender addr2, [4] sender phone,
 * [5] purpose, [6] reference, [7] recipient name, [8] recipient addr1, [9] recipient addr2,
 * [10] sender account, [11] recipient account(s), [12] amount, [13] currency, [14] priority
 */
private fun parsePaymentInstruction(text: String): PaymentInstruction {
    val lines = text.split("\n")

    val senderName = lines[1]
    val senderAddr1 = lines[2]
    val senderAddr2 = lines[3]
    val senderPhone = lines[4]
    val senderAccount = lines[10]
    val hasSender = listOf(senderName, senderAddr1, senderAddr2, senderAccount).any { it.isNotEmpty() }

    val sender =
        if (hasSender) {
            Sender(
                name = Name(senderName),
                address =
                    Address(
                        addressLine1 = AddressLine1.of(senderAddr1, ""),
                        addressLine2 = AddressLine2.of(senderAddr2, ""),
                    ),
                account = Account(senderAccount),
                phoneNumber = if (senderPhone.isEmpty()) null else PhoneNumber.of(senderPhone),
            )
        } else {
            null
        }

    return PaymentInstruction(
        sender = sender,
        recipient =
            Recipient(
                name = Name(lines[7]),
                address =
                    Address(
                        addressLine1 = AddressLine1.of(lines[8], ""),
                        addressLine2 = AddressLine2.of(lines[9], ""),
                    ),
                account = RecipientAccount.of(lines[11].split(",").map { Account(it) }),
            ),
        purpose = PaymentPurpose.of(lines[5]),
        reference = if (lines[6].isEmpty()) null else PaymentReference(lines[6]),
        amount = Amount.of(lines[12]),
        paymentPriority = if (lines[14] == "D") PaymentPriority.urgent() else PaymentPriority.regular(),
    )
}
