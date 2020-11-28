package com.example.currencycalculator.converter

import com.example.currencycalculator.ui.convert.convertCurrency
import com.example.currencycalculator.ui.convert.getImageUrl
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class ConverterUtilTest {

    @Test
    fun `convert GHS Cedis to Naira NGN`() {
        val cedisRate = 6.920055
        val nairaRate = 452.042196

        val value = convertCurrency("1", cedisRate, nairaRate)
        assertThat(value, `is`("65.32"))
    }

    @Test
    fun `get nigeria image code`() {
        val symbol = "NGN"
        val url = getImageUrl(symbol)
        assertThat(url, `is`("https://flagcdn.com/28x21/ng.png"))
    }

    /**
     * this actual actual response is expected, so that if it fails
     * glide handles the error and falls back to an image error
     * (same goes for if an image code doesn't exist on flagcdn)
     */
    @Test
    fun `get image code when empty string is passed`() {
        val symbol = ""
        val url = getImageUrl(symbol)
        assertThat(url, `is`(" "))
    }

    @Test
    fun `get image code when invalid string is passed`() {
        val symbol = "a"
        val url = getImageUrl(symbol)
        assertThat(url, `is`(" "))
    }
}