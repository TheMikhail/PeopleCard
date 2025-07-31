package com.example.peoplecard.ViewModel
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peoplecard.model.People
import com.example.peoplecard.network.PeopleApi
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PeopleViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreferences = application.getSharedPreferences("MyPrefs", Application.MODE_PRIVATE)
    val gson = Gson()
    var peopleState:PeopleState by mutableStateOf(PeopleState.Loading)
        private set

    init {
        getPeople()
    }
    fun getPeople(){
        val cachedPeople = getPeopleFromCache()
        if (cachedPeople.isNotEmpty()){
            peopleState = PeopleState.Success(cachedPeople)
        }
        else{
            viewModelScope.launch {
                peopleState = PeopleState.Loading
                try {
                    val response = PeopleApi.retrofitServices.getPeopleFromAPI(50)
                    savePeopleToCache(response.peoples)
                    peopleState = PeopleState.Success(response.peoples)
                }
                catch (e:Exception){
                    peopleState = PeopleState.Error(e.message?:"Unknown Error")
                }
            }
        }

    }
    fun getPeopleFromCache(): List<People>{
        val peopleData = sharedPreferences.getString("people_data", null)
        if (peopleData != null){
            val people = gson.fromJson(peopleData, Array<People>::class.java).toList()
            peopleState = PeopleState.Success(people)
            return people
        }
        else
            return emptyList()
    }
    private fun savePeopleToCache(people: List<People>) {
       sharedPreferences.edit()
           .putString("people_data", gson.toJson(people))
           .apply()
    }
}

sealed interface PeopleState{
    object Loading: PeopleState
    data class Success(val people: List<People>) : PeopleState
    data class Error(val message: String) : PeopleState
}