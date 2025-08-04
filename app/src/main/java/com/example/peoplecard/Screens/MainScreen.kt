package com.example.peoplecard.Screens

import android.annotation.SuppressLint
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.peoplecard.ViewModel.PeopleState
import com.example.peoplecard.ViewModel.PeopleViewModel
import com.example.peoplecard.model.People
import com.example.peoplecard.openContact
import com.example.peoplecard.openEmail
import com.example.peoplecard.openMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun PersonCard(person: People) {
    var showDetail by remember { mutableStateOf(false) }

    Log.d("log", "")
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { showDetail = true }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = person.picture.large,
                contentDescription = "User Photo",
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Name: ${person.name.title} ${person.name.first} ${person.name.last}",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 20.sp
            )
            Text(
                text =
                "Address: ${person.location.city}, " +
                        "${person.location.state}, " +
                        "${person.location.country}, " +
                        "${person.location.postcode}",
                modifier = Modifier.padding(top = 4.dp)
            )
            val context = LocalContext.current
            Text(
                text = "Phone number: ${person.phone}",
                modifier = Modifier
                    .padding(top = 4.dp)
            )
        }
    }
    if (showDetail) {
        ShowDetailCard(
            person = person,
            onDismiss = { showDetail = false }
        )
    }
}

@SuppressLint("SimpleDateFormat")
fun dateParse(date: String): String {
    val currentFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    val newData: Date = currentFormat.parse(date)
    val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val formattedDate = targetFormat.format(newData)
    return formattedDate
}

@Composable
fun ShowDetailCard(person: People, onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "${person.name.title} ${person.name.first} ${person.name.last}")
        },
        text = {
            Column {
                AsyncImage(
                    model = person.picture.large,
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))
                Box (modifier = Modifier
                    .clickable {
                        openMap(context, person.location.coordinates.latitude, person.location.coordinates.longitude)
                    }
                ){
                    Column {
                        Text("Address:", fontWeight = FontWeight.Bold)
                        Text("${person.location.street?.number} ${person.location.street?.name}", style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline
                        ))
                        Text("${person.location.city}, ${person.location.state}", style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline
                        ))
                        Text("${person.location.country}, ${person.location.postcode}", style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline
                        ))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text("Phone: ${person.phone}",
                    modifier = Modifier
                        .clickable {
                            openContact(context, person.phone)
                        }, style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline
                    ))
                Text("Email: ${person.email}", modifier = Modifier
                    .clickable {
                        openEmail(context, person.email)
                    }, style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),)
                Text("Gender: ${person.gender}")
                Text("Login: ${person.login.username}")
                Text("Dob: ${dateParse(person.dob.date)}")
                Text("Age: ${person.dob.age}")
                Text("Registered date: ${dateParse(person.registered.date)}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleList(
    people: List<People>
) {

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(1500)
            isRefreshing = false
        }
    }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(people) { person ->
                PersonCard(person = person)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleScreen(
    state: PeopleState,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("People") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {
                is PeopleState.Loading -> LoadingIndicator()
                is PeopleState.Success -> PeopleList(people = state.people)
                is PeopleState.Error -> ErrorMessage(message = state.message)
            }
        }
    }


}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}