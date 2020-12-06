package com.example.currencycalculator.converter

import com.example.currencycalculator.data.helper.toCurrencyRateList
import com.example.currencycalculator.data.helper.toCurrencySymbolList
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class NetworkResponseParserTest {

    @Test
    fun `convert response to currency symbol list`() {
        val sampleResponse =
            "{\"success\":true,\"symbols\":{\"AED\":\"United Arab Emirates Dirham\",\"AFN\":\"Afghan Afghani\"}}"
        val list = toCurrencySymbolList(sampleResponse)
        assertThat(list.size, `is`(2))
        assertThat(list[0].currency, `is`("AED"))
        assertThat(list[0].name, `is`("United Arab Emirates Dirham"))
    }

    @Test
    fun `convert response to rate list`() {
        val sampleResponse =
            "{\"success\":true,\"timestamp\":1606487655,\"base\":\"EUR\",\"date\":\"2020-11-27\",\"rates\":{\"AED\":4.38489}}"
        val list = toCurrencyRateList(sampleResponse)
        assertThat(list.size, `is`(1))
        assertThat(list[0].currency, `is`("AED"))
        assertThat(list[0].rate, `is`(4.38489))
    }
}