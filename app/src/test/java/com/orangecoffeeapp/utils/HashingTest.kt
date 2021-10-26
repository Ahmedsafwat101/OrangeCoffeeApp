package com.orangecoffeeapp.utils

import com.google.common.truth.Truth.assertThat
import com.orangecoffeeapp.utils.common.Hashing
import org.junit.Test

class HashingTest{
    @Test
    fun `same hashing return true`(){
        val result = Hashing.sha256("Hello world")
        assertThat(result).isEqualTo(Hashing.sha256("Hello world"))
    }


    @Test
    fun `different hashing return false`(){
        val result = Hashing.sha256("Hello world")
        assertThat(result).isNotEqualTo(Hashing.sha256("Another Word"))
    }
}