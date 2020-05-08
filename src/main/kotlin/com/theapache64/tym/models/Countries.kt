package com.theapache64.tym.models

import com.squareup.moshi.Json


data class Countries(
    @Json(name = "countries")
    val countries: List<Country>
) {
    data class Country(
        @Json(name = "id")
        val id: Int, // 1
        @Json(name = "name")
        val name: String, // Afghanistan
        @Json(name = "phoneCode")
        val phoneCode: Int, // 93
        @Json(name = "sortname")
        val sortname: String // AF
    )
}