package com.example.peoplecard

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast

fun openContact(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
        type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
        putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "Контакты не найдены на устройстве",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun openEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "Почтовое приложение не найдено",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@SuppressLint("SuspiciousIndentation")
fun openMap(context: Context, latitude: String, longitude: String) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://maps.google.com/maps?saddr=$latitude&daddr=$longitude")
    )
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "Приложение карт не найдено",
            Toast.LENGTH_SHORT
        ).show()
    }
}