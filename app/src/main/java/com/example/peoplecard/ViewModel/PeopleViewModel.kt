package com.example.peoplecard.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peoplecard.model.People
import com.example.peoplecard.network.PeopleApi
import kotlinx.coroutines.launch

class PeopleViewModel : ViewModel() {
    //private val _peopleState = mutableStateOf<PeopleState>(PeopleState.Loading)
    var peopleState:PeopleState by mutableStateOf(PeopleState.Loading)
        private set
    init {
        getPeople()
    }
    fun getPeople(){
        viewModelScope.launch {
            peopleState = PeopleState.Loading
            try {
                val response = PeopleApi.retrofitServices.getPeopleFromAPI()
                peopleState = PeopleState.Success(response.peoples)
            }
            catch (e:Exception){
                peopleState = PeopleState.Error(e.message?:"Unknown Error")
            }
        }
    }
}

sealed interface PeopleState{
    object Loading: PeopleState
    data class Success(val people: List<People>) : PeopleState
    data class Error(val message: String) : PeopleState
}