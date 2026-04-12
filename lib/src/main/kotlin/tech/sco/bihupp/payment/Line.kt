package tech.sco.bihupp.payment

private val ALLOWED_CHARS_REGEX = Regex("""[^0-9a-zA-ZčćđšžČĆĐŠŽ ,:.?()+'/\-]""")

abstract class Line {
    abstract val value: String

    override fun toString() = "$value$SEPARATOR"

    companion object {
        const val SEPARATOR = "\n"
    }
}

class EmptyLine : Line() {
    override val value = ""
}

internal fun String.checkLengthAndChars(
    line: String,
    maxLength: Int,
    minLength: Int = 0,
) {
    checkLength(line, maxLength, minLength)

    checkAllowedChars(line)
}

internal fun String.checkLength(
    line: String,
    maxLength: Int,
    minLength: Int = 0,
) {
    check(length <= maxLength) {
        "$line exceeds maximum length of $maxLength, got $length."
    }

    if (minLength > 0) {
        check(length >= minLength) {
            "$line must be at least $minLength characters long, got $length."
        }
    }
}

internal fun String.checkAllowedChars(line: String) {
    check(!ALLOWED_CHARS_REGEX.containsMatchIn(this)) {
        "$line contains invalid characters, allowed characters are: 0-9, a-z, A-Z, č, ć, đ, š, ž, Č,Ć,Đ,Š,Ž,,:.?-()+'/"
    }
}
