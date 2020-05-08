package com.theapache64.tym.models

import com.squareup.moshi.Json


data class CountryTimezones(
    @Json(name = "capital")
    val capital: String?, // Harare
    @Json(name = "country_code")
    val countryCode: String, // ZW
    @Json(name = "latlng")
    val latlng: List<Double>,
    @Json(name = "name")
    val name: String, // Zimbabwe
    @Json(name = "timezones")
    val timezones: List<String>
)
