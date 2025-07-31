package com.example.peoplecard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peoplecard.Screens.PeopleScreen
import com.example.peoplecard.ViewModel.PeopleViewModel

@Composable
fun PeopleApp(){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val peopleViewModel: PeopleViewModel = viewModel()
        PeopleScreen(
            state = peopleViewModel.peopleState
        )
    }
}