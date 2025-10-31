package com.hault.codex.ui.addeditcharacter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCharacterScreen(
    navController: NavController,
    viewModel: AddEditCharacterViewModel = hiltViewModel()
) {
    val characterName by viewModel.characterName.collectAsState()
    val characterBackstory by viewModel.characterBackstory.collectAsState()

    Scaffold(
        topBar = {
                        TopAppBar(
                            title = { Text("Create/Edit Character") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Filled.ArrowBack, "Back")
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    viewModel.saveCharacter()
                                    navController.popBackStack()
                                }) {
                                    Icon(Icons.Filled.Check, "Save Character")
                                }
                            }
                        )
                    } ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = characterName,
                            onValueChange = { viewModel.onCharacterNameChange(it) },
                            label = { Text("Character Name") },
                            placeholder = { Text("e.g., Gandalf") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = characterBackstory,
                            onValueChange = { viewModel.onCharacterBackstoryChange(it) },
                            label = { Text("Character Backstory") },
                            placeholder = { Text("Tell us about your character's past...") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = false,
                            minLines = 5
                        )
                    }
                }
            }
