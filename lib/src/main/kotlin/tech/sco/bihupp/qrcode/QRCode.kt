package tech.sco.bihupp.qrcode

import qrcode.QRCode
import tech.sco.bihupp.payment.PaymentInstruction
import kotlin.io.encoding.Base64

fun QRCode.Companion.of(
    payment: PaymentInstruction,
    format: ImageFormat = ImageFormat.PNG,
): QRCodeByteArray =
    when (format) {
        ImageFormat.SVG -> {
            QRCodeByteArray(
                QRCode
                    .ofSquares()
                    .withGraphicsFactory(SVGGraphicsFactory())
                    .build(payment.toString())
                    .renderToBytes(),
                format,
            )
        }

        ImageFormat.PNG -> {
            QRCodeByteArray(
                QRCode
                    .ofSquares()
                    .build(payment.toString())
                    .renderToBytes(),
                format,
            )
        }
    }

data class QRCodeByteArray(
    val bytes: ByteArray,
    val format: ImageFormat,
) {
    override fun equals(other: Any?): Boolean =
        this === other || (other is QRCodeByteArray && bytes.contentEquals(other.bytes) && format == other.format)

    override fun hashCode(): Int = 31 * bytes.contentHashCode() + format.hashCode()
}

enum class ImageFormat {
    SVG,
    PNG,
    ;

    val mime: String get() =
        when (this) {
            SVG -> "image/svg+xml"
            PNG -> "image/png"
        }
}

fun QRCodeByteArray.toBase64Link(): String = "data:${format.mime};base64,${Base64.encode(bytes)}"
