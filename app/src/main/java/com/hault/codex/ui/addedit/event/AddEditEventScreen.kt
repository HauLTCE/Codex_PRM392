package com.hault.codex.ui.addedit.event

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    navController: NavController,
    worldId: Int,
    viewModel: AddEditEventViewModel = hiltViewModel()
) {
    val eventName by viewModel.eventName.collectAsState()
    val eventDate by viewModel.eventDate.collectAsState()
    val eventDescription by viewModel.eventDescription.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add/Edit Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = eventName,
                onValueChange = viewModel::onEventNameChange,
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = eventDate,
                onValueChange = viewModel::onEventDateChange,
                label = { Text("Event Date") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = eventDescription,
                onValueChange = viewModel::onEventDescriptionChange,
                label = { Text("Event Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.saveEvent(worldId)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Event")
            }
        }
    }
}