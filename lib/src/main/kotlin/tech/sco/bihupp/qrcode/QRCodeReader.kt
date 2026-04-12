package tech.sco.bihupp.qrcode

import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import tech.sco.bihupp.payment.PaymentInstruction
import tech.sco.bihupp.payment.splitToLines
import tech.sco.bihupp.payment.toPaymentInstruction

object QRCodeReader {
    @Suppress("TooGenericExceptionCaught")
    fun scan(
        qrcode: QRCodePixels,
        hints: Map<DecodeHintType, Any> =
            mapOf(
                DecodeHintType.CHARACTER_SET to "UTF-8",
                DecodeHintType.TRY_HARDER to true,
            ),
    ): QRCodeScanResult =
        try {
            val source = RGBLuminanceSource(qrcode.width, qrcode.height, qrcode.argbPixels)
            val bitmap = BinaryBitmap(HybridBinarizer(source))

            val payload = MultiFormatReader().decode(bitmap, hints).text

            try {
                QRCodeScanResult.Success(
                    payload.splitToLines().toPaymentInstruction(),
                    payload,
                )
            } catch (e: Exception) {
                QRCodeScanResult.Failure(e, payload)
            }
        } catch (e: Exception) {
            QRCodeScanResult.Failure(e, null)
        }
}

sealed interface QRCodeScanResult {
    data class Success(
        val paymentInstruction: PaymentInstruction,
        val rawPayload: String,
    ) : QRCodeScanResult

    data class Failure(
        val error: Throwable,
        val rawPayload: String?,
    ) : QRCodeScanResult
}

data class QRCodePixels(
    val width: Int,
    val height: Int,
    val argbPixels: IntArray,
) {
    override fun equals(other: Any?): Boolean =
        this === other ||
            (
                other is QRCodePixels &&
                    width == other.width &&
                    height == other.height &&
                    argbPixels.contentEquals(other.argbPixels)
            )

    override fun hashCode(): Int = 31 * (31 * width + height) + argbPixels.contentHashCode()
}
