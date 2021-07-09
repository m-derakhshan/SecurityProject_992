package m.derakhshan.mybank


import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Cryptography {


    fun encrypt(
        plaintext: ByteArray?, key: SecretKey, IV: ByteArray?
    ): ByteArray? {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        val keySpec = SecretKeySpec(key.encoded, "AES")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(plaintext)
    }

    fun decrypt(cipherText: ByteArray?, key: SecretKey, IV: ByteArray?): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedText = cipher.doFinal(cipherText)
            return String(decryptedText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getSecretKey(key: String): SecretKey{
        return SecretKeySpec(key.toByteArray(), "AES")
    }


    /*
    fun encrypt(input: String, key: String): String {
        return try {
            Log.e("Log", "encrypt iv:$initVector")

            val plainText = input.toByteArray()


            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            val keySpec = SecretKeySpec(key.toByteArray(charset), "AES")


            cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(initVector))
            val encrypt = cipher.doFinal(plainText)

            //Base64.encodeToString(encrypt, Base64.NO_WRAP)
            String(encrypt)
        } catch (e: Exception) {
            Log.d("Log", "Error in encrypt: $e")
            ""
        }

    }

    fun decrypt(input: String, key: String): String {
        return try {
            Log.e("Log", "decrypt iv:$initVector")

            val cypherText = Base64.decode(input, Base64.NO_WRAP)


            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            val keySpec = SecretKeySpec(key.toByteArray(charset), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(initVector))
            val decrypt = cipher.doFinal(cypherText)


            String(decrypt)

        } catch (e: Exception) {
            Log.d("Log", "Error in decrypt: $e")
            ""
        }
    }
*/
}