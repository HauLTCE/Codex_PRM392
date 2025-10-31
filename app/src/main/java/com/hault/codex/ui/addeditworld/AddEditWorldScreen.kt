package com.hault.codex.ui.addeditworld

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWorldScreen(
    navController: NavController,
    viewModel: AddEditWorldViewModel = hiltViewModel()
) {
    val worldName by viewModel.worldName.collectAsState()
    val worldDescription by viewModel.worldDescription.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create/Edit World") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
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
                placeholder = { Text("e.g., Middle-earth") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            OutlinedTextField(
                value = worldDescription,
                onValueChange = { viewModel.worldDescription.value = it },
                label = { Text("World Description") },
                placeholder = { Text("A brief description of your world...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 5
            )
        }
    }
}