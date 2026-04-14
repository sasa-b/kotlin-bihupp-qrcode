package tech.sco.bihupp

import qrcode.QRCode
import tech.sco.bihupp.payment.Account
import tech.sco.bihupp.payment.Address
import tech.sco.bihupp.payment.AddressLine1
import tech.sco.bihupp.payment.AddressLine2
import tech.sco.bihupp.payment.Amount
import tech.sco.bihupp.payment.Name
import tech.sco.bihupp.payment.PaymentInstruction
import tech.sco.bihupp.payment.PaymentPurpose
import tech.sco.bihupp.payment.PaymentReference
import tech.sco.bihupp.payment.Recipient
import tech.sco.bihupp.payment.RecipientAccount
import tech.sco.bihupp.payment.Sender
import tech.sco.bihupp.qrcode.QRCodePixels
import tech.sco.bihupp.qrcode.QRCodeReader
import tech.sco.bihupp.qrcode.QRCodeScanResult
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class QRCodeReaderTest {
    /**
     * Getting image pixels is platform-specific, test cases covers the JVM Server Side example.
     * On Android it would look something like:
     *
     *  val argbPixels = IntArray(bitmap.width * bitmap.height)
     *   bitmap.getPixels(
     *       argbPixels, // destination array
     *       0, // offset — start writing at index 0
     *       bitmap.width, // stride — row width (same as scansize above)
     *       0, // startX
     *       0, // startY
     *       bitmap.width, // width — read all columns
     *       bitmap.height, // height — read all rows
     *   )
     */
    @Test
    fun `it scans example png and decodes it to the expected payment instruction`() {
        val image = ImageIO.read(javaClass.getResourceAsStream("/example.png"))
        val pixels =
            QRCodePixels(
                width = image.width,
                height = image.height,
                argbPixels =
                    image.getRGB(
                        0, // startX - Start at the leftmost column
                        0, // startY - Start at the topmost row
                        image.width, // Read all columns
                        image.height, // Read all rows
                        null, // destination array - Don't reuse an existing array — allocate a new one and return it
                        0, // offset - Write into the new array starting at index 0
                        image.width, // scanline width - Each row in the output array is image.width pixels wide
                    ),
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

        assertContains(
            result.rawPayload,
            """
            BIHUPP10
            Marko Marković
            Ulica Meše Selimovića 12
            78000 Banja Luka

            Račun za el. energiju
            2024001
            Pero Perić
            Titova 1
            71000 Sarajevo
            1234567890123456
            9876543210987654
            000000000010000
            BAM
            N
            """.trimIndent(),
        )
    }

    @Test
    fun `returns failure with null payload when image contains no qr code`() {
        val width = 100
        val height = 100
        val pixels =
            QRCodePixels(
                width = width,
                height = height,
                argbPixels = IntArray(width * height) { 0xFFFFFFFF.toInt() },
            )

        val result = QRCodeReader.scan(pixels)

        assertIs<QRCodeScanResult.Failure>(result)
        assertNull(result.rawPayload)
    }

    @Test
    fun `it returns failure with raw payload when qr code content is not a valid bihupp payload`() {
        val content = "not a valid BIHUPP payload"
        val pngBytes = QRCode.ofSquares().build(content).renderToBytes()
        val image = ImageIO.read(ByteArrayInputStream(pngBytes))
        val pixels =
            QRCodePixels(
                width = image.width,
                height = image.height,
                argbPixels = image.getRGB(0, 0, image.width, image.height, null, 0, image.width),
            )

        val result = QRCodeReader.scan(pixels)

        assertIs<QRCodeScanResult.Failure>(result)
        assertEquals(content, result.rawPayload)
    }
}
