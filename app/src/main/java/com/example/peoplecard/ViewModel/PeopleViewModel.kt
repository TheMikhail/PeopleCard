package com.example.peoplecard.ViewModel

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.peoplecard.model.People
import com.example.peoplecard.network.PeopleApi
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PeopleViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences =
        application.getSharedPreferences("MyPrefs", Application.MODE_PRIVATE)
    private val gson = Gson()
    var peopleState: PeopleState by mutableStateOf(PeopleState.Loading())
        private set

    init {
        getPeople()
    }

    fun getPeople(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!forceRefresh) {
                val cachedPeople = getPeopleFromCache()
                if (cachedPeople.isNotEmpty()) {
                    peopleState = PeopleState.Success(cachedPeople)
                    return@launch
                }
            }
            peopleState = PeopleState.Loading(isRefresh = true)
            try {
                val response = PeopleApi.retrofitServices.getPeopleFromAPI(50)
                savePeopleToCache(response.peoples)
                peopleState = PeopleState.Success(response.peoples)
                Log.d("SAVE_DATA", "Saving people: ${gson.toJson(response.peoples)}")
            } catch (e: Exception) {
                val cachedPeople = getPeopleFromCache()
                if (cachedPeople.isNotEmpty() && !forceRefresh)
                    peopleState = PeopleState.Success(cachedPeople)
                else
                    peopleState = PeopleState.Error(e.message ?: "Unknown Error")

            }
        }
    }


    private fun getPeopleFromCache(): List<People> {
        val peopleData = sharedPreferences.getString("people_data", null)
        if (peopleData != null) {
            val people = gson.fromJson(peopleData, Array<People>::class.java).toList()
            peopleState = PeopleState.Success(people)
            return people
        } else
            return emptyList()
    }

    private fun savePeopleToCache(people: List<People>) {
        sharedPreferences.edit()
            .putString("people_data", gson.toJson(people))
            .apply()

    }


}

sealed interface PeopleState {
    data class Loading(val isRefresh: Boolean = false) : PeopleState
    data class Success(val people: List<People>) : PeopleState
    data class Error(val message: String) : PeopleState
}