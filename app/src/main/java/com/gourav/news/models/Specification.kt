package com.gourav.news.models

import com.gourav.news.network.NewsApi

import java.util.Locale

class Specification {

    var category: String = "Technology"
        private set
    var country = Locale.getDefault().country.toLowerCase()
    var language: String? = null

    fun setCategory(category: NewsApi.Category) {
        this.category = category.name
    }

    companion object {
        // List of available countries
        private val COUNTRIES_AVAILABLE = arrayOf("ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za")
    }
}
