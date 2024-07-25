package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search for a city") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = {
            // Handle search action
        }) {
            Text("Search")
        }
    }
}
