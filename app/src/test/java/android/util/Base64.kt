package android.util

import java.util.Base64 as JavaBase64

/**
 * JVM-only stand-in for the Android SDK class, whose local-test implementation is a stub.
 * The flags used by the application are preserved so conversion behavior remains testable.
 */
class Base64 private constructor() {
    companion object {
        @JvmField
        val DEFAULT: Int = 0
        @JvmField
        val NO_PADDING: Int = 1
        @JvmField
        val NO_WRAP: Int = 2
        @JvmField
        val CRLF: Int = 4
        @JvmField
        val URL_SAFE: Int = 8
        @JvmField
        val NO_CLOSE: Int = 16

        @JvmStatic
        fun encode(input: ByteArray, flags: Int): ByteArray {
            val encoder = if (flags and URL_SAFE != 0) {
                JavaBase64.getUrlEncoder()
            } else {
                JavaBase64.getEncoder()
            }.let { if (flags and NO_PADDING != 0) it.withoutPadding() else it }

            var encoded = encoder.encodeToString(input)
            if (flags and NO_WRAP == 0 && flags and CRLF != 0 && encoded.length > 76) {
                encoded = encoded.chunked(76).joinToString("\r\n")
            }
            return encoded.toByteArray(Charsets.UTF_8)
        }

        @JvmStatic
        fun decode(input: ByteArray, flags: Int): ByteArray {
            val normalized = input.toString(Charsets.UTF_8).filterNot { it == '\n' || it == '\r' }
            val decoder = if (flags and URL_SAFE != 0) {
                JavaBase64.getUrlDecoder()
            } else {
                JavaBase64.getDecoder()
            }
            return decoder.decode(normalized)
        }
    }
}
