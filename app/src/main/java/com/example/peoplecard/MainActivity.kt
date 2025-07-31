package com.example.peoplecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.peoplecard.Screens.PeopleScreen
import com.example.peoplecard.ViewModel.PeopleViewModel
import com.example.peoplecard.model.People
import com.example.peoplecard.network.PeopleApi
import com.example.peoplecard.ui.theme.PeopleCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeopleCardTheme {
                PeopleApp()
            }
        }
    }
}
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
