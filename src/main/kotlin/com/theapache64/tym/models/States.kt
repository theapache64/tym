package com.theapache64.tym.models

import com.squareup.moshi.Json


data class States(
    @Json(name = "states")
    val states: List<State>
) {
    data class State(
        @Json(name = "country_id")
        val countryId: String, // 101
        @Json(name = "id")
        val id: String, // 1
        @Json(name = "name")
        val name: String // Andaman and Nicobar Islands
    )
}