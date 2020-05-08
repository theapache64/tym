package com.theapache64.tym.models

import com.squareup.moshi.Json


data class Cities(
    @Json(name = "cities")
    val cities: List<City>
) {
    data class City(
        @Json(name = "id")
        val id: String, // 1
        @Json(name = "name")
        val name: String, // Bombuflat
        @Json(name = "state_id")
        val stateId: String // 1
    )
}