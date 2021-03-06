package ru.SnowVolf.convertx.algorhitms


import android.util.Log
import ru.SnowVolf.convertx.settings.DefStrings
import java.math.BigInteger
import java.security.MessageDigest
import java.security.MessageDigest.getInstance
import java.security.NoSuchAlgorithmException

/**
 * Created by Snow Volf on 18.02.2017.
 */

object ChecksumAlgs {
    /**
     * В оф документации по Android есть заметка, что SHA-224 добавили в API 1, в API 9 убрали,
     * потом снова вернули в API 22.
     * Почему так -- хз. Не стал генерить еще один catch, их тут и так достаточно.

     * Поэтому просто показываем сообщение, если алгоритм в getInstance()
     * не существует.
     */
    private val errAlg = "Неизвестный алгоритм. SDK < 22"

    fun MD5(MD: String): String {
        val messageDigest: MessageDigest
        val digest: ByteArray//new byte[0] можно не использовать

        try {
            messageDigest = getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(MD.toByteArray())
            digest = messageDigest.digest()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(DefStrings.TAG, "errAlg : MD5")
            return errAlg
        }

        val bigInt = BigInteger(1, digest)
        var result = bigInt.toString(16)

        while (result.length < 32) {
            result = "0" + result
        }
        return result
    }

    fun SHA1(SHA1: String): String {
        val messageDigest: MessageDigest
        val digest: ByteArray
        try {
            messageDigest = getInstance("SHA-1")
            messageDigest.reset()
            messageDigest.update(SHA1.toByteArray())
            digest = messageDigest.digest()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(DefStrings.TAG, "errAlg : SHA-1")
            return errAlg
        }

        val bigInt = BigInteger(1, digest)
        var result = bigInt.toString(16)

        while (result.length < 32) {
            result = "0" + result
        }
        return result
    }

    fun SHA224(SHA224: String): String {
        val messageDigest: MessageDigest
        val digest: ByteArray

        try {
            messageDigest = getInstance("SHA-224")
            messageDigest.reset()
            messageDigest.update(SHA224.toByteArray())
            digest = messageDigest.digest()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(DefStrings.TAG, "errAlg : SHA-224")
            return errAlg
        }

        val bigInt = BigInteger(1, digest)
        var result = bigInt.toString(16)

        while (result.length < 32) {
            result = "0" + result
        }
        return result
    }

    fun SHA384(SHA384: String): String {
        val messageDigest: MessageDigest
        val digest: ByteArray

        try {
            messageDigest = getInstance("SHA-384")
            messageDigest.reset()
            messageDigest.update(SHA384.toByteArray())
            digest = messageDigest.digest()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(DefStrings.TAG, "errAlg : SHA-384")
            return errAlg
        }

        val bigInt = BigInteger(1, digest)
        var result = bigInt.toString(16)

        while (result.length < 32) {
            result = "0" + result
        }
        return result
    }

    fun SHA512(SHA512: String): String {
        val messageDigest: MessageDigest
        val digest: ByteArray

        try {
            messageDigest = getInstance("SHA-512")
            messageDigest.reset()
            messageDigest.update(SHA512.toByteArray())
            digest = messageDigest.digest()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(DefStrings.TAG, "errAlg : SHA-512")
            return errAlg
        }

        val bigInt = BigInteger(1, digest)
        var result = bigInt.toString(16)

        while (result.length < 32) {
            result = "0" + result
        }
        return result
    }

}
