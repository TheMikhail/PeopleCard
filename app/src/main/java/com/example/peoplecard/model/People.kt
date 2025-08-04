package com.example.peoplecard.model

import androidx.compose.animation.core.R
import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class PeopleResponse(
    @SerializedName("results") val peoples: List<People>
)
data class Id(
    val name: String,
    val value:String
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
    val number:Int? = null,
    val name:String? = null

)
data class Location(
    val street: Street? = null,
    val city: String? = null,
    val state: String? = null,
    val postcode: String? = null,
    val country: String? = null,
)
data class Registered(
    val date: String,
    val age: Int
)
data class Login(
    val uuid:String,
    val username:String,
    val password:String,
    val salt:String,
    val md5:String,
    val sha1:String,
    val sha256:String,
)
data class Dob(
    val date: String,
    val age: Int
)
data class People(
    val gender:String,
    val login:Login,
    val name: Name,
    val picture: Picture,
    val location: Location,
    val phone: String? = null,
    val email:String,
    val dob: Dob,
    val registered: Registered ,
)