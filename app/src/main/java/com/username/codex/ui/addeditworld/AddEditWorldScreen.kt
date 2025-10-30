package com.username.codex.ui.addeditworld

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddEditWorldScreen(
    navController: NavController,
    viewModel: AddEditWorldViewModel = hiltViewModel()
) {
    val worldName by viewModel.worldName.collectAsState()

    Scaffold(
        floatingActionButton = {
            Button(onClick = { 
                viewModel.saveWorld()
                navController.popBackStack()
            }) {
                Text("Save")
            }
        }
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {
            OutlinedTextField(
                value = worldName,
                onValueChange = { viewModel.worldName.value = it },
                label = { Text("World Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
