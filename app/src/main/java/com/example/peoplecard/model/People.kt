package com.example.peoplecard.model

import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("results") val peoples: List<People>
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
data class People(
    val name: Name,
    val picture: Picture,
    val location: Location,
    val phone: String? = null
)
