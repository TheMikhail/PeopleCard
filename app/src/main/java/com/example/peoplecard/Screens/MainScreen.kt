package com.example.peoplecard.Screens

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.peoplecard.ViewModel.PeopleState
import com.example.peoplecard.model.People

@Composable
fun PersonCard(person:People){
    var showDetail by remember { mutableStateOf(false) }

    Card(Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable { showDetail = true }) {
        Column(modifier = Modifier.padding(16.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = person.picture.large,
                contentDescription = "User Photo",
                modifier = Modifier.padding(8.dp)
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(text = "Name: ${person.name.title} ${person.name.first} ${person.name.last}",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 20.sp)
            Text(text =
                    "Address: ${person.location.city}, " +
                    "${person.location.state}, " +
                    "${person.location.country}, " +
                    "${person.location.postcode}",
                modifier = Modifier.padding(top = 4.dp))
            Text(
                text = "Phone number: ${person.phone}",
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
    if (showDetail){
        ShowDetailCard(
            person = person,
            onDismiss = {showDetail = false}
        )
    }
}
@Composable
fun ShowDetailCard(person: People, onDismiss: () -> Unit){
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

                Text("Address:", fontWeight = FontWeight.Bold)
                Text("${person.location.street?.number} ${person.location.street?.name}")
                Text("${person.location.city}, ${person.location.state}")
                Text("${person.location.country}, ${person.location.postcode}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Phone: ${person.phone}")
                Text("Gender: ${person.gender}")
                Text("Login: ${person.login.username}")
                Text("Email: ${person.email}")
               // Text("Dob: ${person.dob.date}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun PeopleList(people: List<People>){
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(people) { person ->
            PersonCard(person = person)
        }
    }
}
@Composable
fun PeopleScreen(state : PeopleState){
        when(state){
            is PeopleState.Loading -> LoadingIndicator()
            is PeopleState.Success -> PeopleList(people = state.people)
            is PeopleState.Error -> ErrorMessage(message = state.message)
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