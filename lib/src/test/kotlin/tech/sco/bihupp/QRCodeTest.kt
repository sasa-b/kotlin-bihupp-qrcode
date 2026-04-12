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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QRCodeTest {
    private val payment =
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

    @Test
    fun `converts to a QRCode in PNG format`() {
        val result = QRCode.of(payment, ImageFormat.PNG)

        assertEquals(ImageFormat.PNG, result.format)
        assertTrue(result.bytes.isNotEmpty())
        // verify PNG magic bytes
        val pngSignature = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47)
        assertTrue(
            result.bytes
                .take(4)
                .toByteArray()
                .contentEquals(pngSignature),
        )
    }

    @Test
    fun `converts to a QRCode in SVG format`() {
        val result = QRCode.of(payment, ImageFormat.SVG)

        assertEquals(ImageFormat.SVG, result.format)
        assertTrue(result.bytes.isNotEmpty())
        assertTrue(String(result.bytes).contains("<svg"))
    }

    @Test
    fun `converts to a PNG QRCode that is a Base64 link`() {
        val result = QRCode.of(payment, ImageFormat.PNG).toBase64Link()

        assertTrue(result.startsWith("data:image/png;base64,"))
    }

    @Test
    fun `converts to a SVG QRCode that is a Base64 link`() {
        val result = QRCode.of(payment, ImageFormat.SVG).toBase64Link()

        assertTrue(result.startsWith("data:image/svg+xml;base64,"))
    }
}
