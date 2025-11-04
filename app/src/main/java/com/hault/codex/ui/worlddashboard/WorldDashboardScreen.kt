package com.hault.codex.ui.worlddashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hault.codex.data.model.Character
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import com.hault.codex.ui.timeline.TimelineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldDashboardScreen(
    navController: NavController,
    viewModel: WorldDashboardViewModel = hiltViewModel(),
    timelineViewModel: TimelineViewModel = hiltViewModel()
) {
    val characters by viewModel.characters.collectAsState()
    val locations by viewModel.locations.collectAsState()
    val events by timelineViewModel.events.collectAsState()
    val worldName by viewModel.worldName.collectAsState()
    val worldId = viewModel.worldId
    var showDeleteCharacterDialog by remember { mutableStateOf<Character?>(null) }
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Characters", "Locations", "Timeline")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(worldName) }
            )
        },
        floatingActionButton = {
            if (tabIndex == 2) {
                FloatingActionButton(onClick = { navController.navigate("add_edit_event/${viewModel.worldId}") }) {
                    Icon(Icons.Default.Add, "Add new event")
                }
            } else if (tabIndex == 0) {
                FloatingActionButton(onClick = { navController.navigate("add_character/${viewModel.worldId}") }) {
                    Icon(Icons.Default.Add, "Add new character")
                }
            } else {
                FloatingActionButton(onClick = { navController.navigate("add_edit_location?worldId=$worldId&locationId=-1") }) {
                    Icon(Icons.Default.Add, "Add new location")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> CharactersList(characters, locations, showDeleteCharacterDialog, { showDeleteCharacterDialog = it }, { viewModel.deleteCharacter(it) })
                1 -> LocationsList(locations)
                2 -> TimelineList(events)
            }
        }

        showDeleteCharacterDialog?.let { characterToDelete ->
            AlertDialog(
                onDismissRequest = { showDeleteCharacterDialog = null },
                title = { Text("Delete Character") },
                text = { Text("Are you sure you want to delete ${characterToDelete.name}?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteCharacter(characterToDelete)
                        showDeleteCharacterDialog = null
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteCharacterDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun CharactersList(
    characters: List<Character>,
    locations: List<com.hault.codex.data.model.Location>,
    showDeleteCharacterDialog: Character?,
    setShowDeleteCharacterDialog: (Character?) -> Unit,
    deleteCharacter: (Character) -> Unit
) {
    if (characters.isEmpty()) {
        Text(
            text = "No characters found. Click the '+' button to add one.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn {
            items(characters) { character ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        character.homeLocationId?.let { homeLocationId ->
                            val locationName = locations.find { it.id == homeLocationId }?.name
                            if (locationName != null) {
                                Text(
                                    text = "Home: $locationName",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    IconButton(onClick = { setShowDeleteCharacterDialog(character) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete Character")
                    }
                }
            }
        }
    }
}

@Composable
fun LocationsList(locations: List<com.hault.codex.data.model.Location>) {
    if (locations.isEmpty()) {
        Text(
            text = "No locations found. Click the '+' button to add one.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn {
            items(locations) { location ->
                Text(
                    text = location.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun TimelineList(events: List<com.hault.codex.data.model.Event>) {
    if (events.isEmpty()) {
        Text(
            text = "No events found. Click the '+' button to add one.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn {
            items(events) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.date,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(2f)
                    )
                }
            }
        }
    }
}
