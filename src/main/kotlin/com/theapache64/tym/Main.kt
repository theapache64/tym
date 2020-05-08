package com.theapache64.tym

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.theapache64.gpm.utils.InputUtils
import com.theapache64.tym.models.Countries
import com.theapache64.tym.models.CountryTimezones
import com.theapache64.tym.models.States
import com.theapache64.tym.utils.ANSI_GREEN
import com.theapache64.tym.utils.JarUtils
import com.theapache64.tym.utils.getWithColor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private val moshi by lazy {
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

private val countries by lazy {
    toModel("assets/countries.json", Countries::class.java)
}

private val states by lazy {
    toModel("assets/states.json", States::class.java)
}

private val countriesTimezones by lazy {
    val countriesJson = read("assets/countries_timezones.json")
    val classType = Types.newParameterizedType(List::class.java, CountryTimezones::class.java)
    val adapter: JsonAdapter<List<CountryTimezones>> = moshi.adapter(classType)
    adapter.fromJson(countriesJson)
}

fun <T> toModel(filePath: String, classType: Class<T>): T? {
    return moshi.adapter(classType).fromJson(read(filePath))
}

fun read(filePath: String): String {
    return File("${JarUtils.getJarDir()}$filePath").readText()
}

class Main

fun main(args: Array<String>) {

    if (args.isNotEmpty()) {

        val inputPlace = args.first().toLowerCase().trim()

        // Finding country
        var country = countries!!.countries.find {
            it.name.toLowerCase() == inputPlace ||
                    it.sortname.toLowerCase() == inputPlace ||
                    (it.phoneCode == inputPlace.toIntOrNull())
        }

        if (country == null) {

            // it wasn't a country name, so let's search in states
            val state = states!!.states.find {
                it.name.toLowerCase() == inputPlace
            }

            if (state != null) {
                // find parent country
                country = countries!!.countries.find {
                    state.countryId.trim().toInt() == it.id
                }
            }
        }

        if (country != null) {
            showTimeFor(country)
        } else {
            println("ERROR : Invalid place name '$inputPlace'")
        }

    } else {
        println("ERROR : Pass some place name. eg: tym India, tym Kochi, tym USA, tym California etc.")
    }
}

private val dateFormat by lazy {
    SimpleDateFormat("HH:mm:ss, EEE dd-MM-yyyy")
}

fun showTimeFor(country: Countries.Country) {

    println("Country : ${country.name}, ${country.sortname}")
    val countryTimezone = countriesTimezones!!.find {
        it.countryCode.trim().toLowerCase() == country.sortname.trim().toLowerCase() &&
                it.name.trim().toLowerCase() == country.name.trim().toLowerCase()
    }

    if (countryTimezone != null) {

        val timezones = countryTimezone.timezones
        val timeZoneIndex = if (timezones.size > 1) {
            for ((index, timezone) in timezones.withIndex()) {
                println("${index + 1}) $timezone")
            }
            InputUtils.getInt("Choose timezone", 1, timezones.size) - 1
        } else {
            0
        }

        val timeZone = TimeZone.getTimeZone(timezones[timeZoneIndex])
        dateFormat.timeZone = timeZone
        while (true) {
            val dateSplit = dateFormat.format(Date()).split(",")
            val time = dateSplit[0].trim()
            val date = dateSplit[1].trim()
            print("\rTime is ${getWithColor(ANSI_GREEN, time)}, $date")
            Thread.sleep(1000)
        }

    } else {
        println("ERROR : Couldn't find timezone for country ${country.name}")
    }
}
