package com.example.peoplecard.model

import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("results") val peoples: List<People>
)

data class Id(
    val name: String,
    val value: String
)

data class Name(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null
)

data class Picture(
    val large: String? = null,
    val medium: String? = null,
    val thumbnail: String? = null
)

data class Street(
    val number: Int? = null,
    val name: String? = null

)

data class Coordinates(
    val latitude: String,
    val longitude: String
)

data class Location(
    val street: Street? = null,
    val city: String? = null,
    val state: String? = null,
    val postcode: String? = null,
    val country: String? = null,
    val coordinates: Coordinates
)

data class Registered(
    val date: String,
    val age: Int
)

data class Login(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String,
)

data class Dob(
    val date: String,
    val age: Int
)

data class People(
    val gender: String,
    val login: Login,
    val name: Name,
    val picture: Picture,
    val location: Location,
    val phone: String,
    val email: String,
    val dob: Dob,
    val registered: Registered,
)