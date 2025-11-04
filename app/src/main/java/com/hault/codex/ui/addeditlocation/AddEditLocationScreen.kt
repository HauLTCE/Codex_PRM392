package com.hault.codex.ui.addeditlocation

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hault.codex.ui.theme.CodexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditLocationScreen(
    navController: NavController,
    viewModel: AddEditLocationViewModel = hiltViewModel()
) {
    val locationName by viewModel.locationName.collectAsState()
    val locationDescription by viewModel.locationDescription.collectAsState()

    CodexTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Create/Edit Location") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = locationName,
                    onValueChange = { viewModel.locationName.value = it },
                    label = { Text("Location Name") },
                    placeholder = { Text("e.g., The Shire") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = locationDescription,
                    onValueChange = { viewModel.locationDescription.value = it },
                    label = { Text("Location Description") },
                    placeholder = { Text("A brief description of your location...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 5
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = {
                        viewModel.saveLocation()
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}