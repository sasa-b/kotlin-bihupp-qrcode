package tech.sco.bihupp

import org.jfree.svg.SVGGraphics2D
import qrcode.QRCode
import qrcode.render.QRCodeGraphics
import qrcode.render.QRCodeGraphicsFactory
import tech.sco.bihupp.payment.PaymentInstruction
import java.awt.Graphics2D
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QRCodeByteArray

        if (!bytes.contentEquals(other.bytes)) return false
        if (format != other.format) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + format.hashCode()
        return result
    }
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

/**
 * Class that'll create the [SVGQRCodeGraphics] instances that'll be used to draw the QRCode
 */
class SVGGraphicsFactory : QRCodeGraphicsFactory() {
    override fun newGraphics(
        width: Int,
        height: Int,
    ): QRCodeGraphics = SVGQRCodeGraphics(width, height)
}

class SVGQRCodeGraphics(
    width: Int,
    height: Int,
) : QRCodeGraphics(width, height) {
    private val svgGraphics2D = SVGGraphics2D(width.toDouble(), height.toDouble())

    override fun createGraphics(): Graphics2D = svgGraphics2D

    override fun nativeImage(): Any = svgGraphics2D

    override fun writeImage(
        destination: OutputStream,
        format: String,
    ) {
        OutputStreamWriter(destination, StandardCharsets.UTF_8).use {
            it.write(svgGraphics2D.svgDocument)
        }
    }
}
