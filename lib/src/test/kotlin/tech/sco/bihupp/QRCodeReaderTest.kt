package tech.sco.bihupp

import tech.sco.bihupp.payment.Account
import tech.sco.bihupp.payment.Address
import tech.sco.bihupp.payment.AddressLine1
import tech.sco.bihupp.payment.AddressLine2
import tech.sco.bihupp.payment.Amount
import tech.sco.bihupp.payment.Name
import tech.sco.bihupp.payment.PaymentInstruction
import tech.sco.bihupp.payment.PaymentPurpose
import tech.sco.bihupp.payment.PaymentReference
import tech.sco.bihupp.payment.PhoneNumber
import tech.sco.bihupp.payment.Recipient
import tech.sco.bihupp.payment.RecipientAccount
import tech.sco.bihupp.payment.Sender
import tech.sco.bihupp.qrcode.QRCodePixels
import tech.sco.bihupp.qrcode.QRCodeReader
import tech.sco.bihupp.qrcode.QRCodeScanResult
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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

        val result = QRCodeReader.scan(pixels)

        assertIs<QRCodeScanResult.Success>(result)

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
                        phoneNumber = null,
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

        assertEquals(expected.toString(), result.paymentInstruction.toString())
    }
}
