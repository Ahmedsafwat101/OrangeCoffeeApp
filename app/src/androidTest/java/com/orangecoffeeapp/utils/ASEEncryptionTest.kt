package com.orangecoffeeapp.utils

import com.google.common.truth.Truth
import org.junit.Test

class ASEEncryptionTest{
    @Test
    fun encryptionAndDecryptionSameStringShouldReturnTrue(){
        val encryptedResult =  ASEEncryption.encrypt("Hello world")
        val decryptedResult = ASEEncryption.decrypt(encryptedResult.toString())
        Truth.assertThat(decryptedResult.toString()).isEqualTo("Hello world")
    }


    @Test
    fun encryptionAndDecryptionDifferentStringShouldReturnFalse(){
        val result =  ASEEncryption.encrypt("Hello world").let {it.toString()}
        Truth.assertThat(ASEEncryption.decrypt(result)).isNotEqualTo("Another Word")
    }
}