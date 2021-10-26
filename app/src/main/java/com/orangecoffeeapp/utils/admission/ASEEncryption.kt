package com.orangecoffeeapp.utils.admission

import android.util.Base64
import com.orangecoffeeapp.constants.Constants.IV
import com.orangecoffeeapp.constants.Constants.SALT
import com.orangecoffeeapp.constants.Constants.SECRET_KEY
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class ASEEncryption {
    companion object {

        fun encrypt(strToEncrypt: String) :  String?
        {
            try
            {
                val ivParameterSpec = IvParameterSpec(Base64.decode(IV, Base64.DEFAULT))
                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val spec =  PBEKeySpec(SECRET_KEY.toCharArray(), Base64.decode(SALT, Base64.DEFAULT), 10000, 256)
                val tmp = factory.generateSecret(spec)
                val secretKey =  SecretKeySpec(tmp.encoded, "AES")

                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

                return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
            }
            catch (e: Exception)
            {
                println("Error while encrypting: $e")
            }

            return null
        }

        fun decrypt(strToDecrypt : String) : String? {
            try
            {

                val ivParameterSpec =  IvParameterSpec(Base64.decode(IV, Base64.DEFAULT))

                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val spec =  PBEKeySpec(SECRET_KEY.toCharArray(), Base64.decode(SALT, Base64.DEFAULT), 10000, 256)
                val tmp = factory.generateSecret(spec);
                val secretKey =  SecretKeySpec(tmp.encoded, "AES")

                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
                return  String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
            }
            catch (e : Exception) {
                println("Error while decrypting: $e");
            }
            return null
        }
    }
}



