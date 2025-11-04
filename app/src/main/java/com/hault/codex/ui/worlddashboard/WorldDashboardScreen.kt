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
import com.hault.codex.ui.theme.CodexTheme
import androidx.compose.material.icons.automirrored.filled.ArrowBack

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
    var showDeleteLocationDialog by remember { mutableStateOf<com.hault.codex.data.model.Location?>(null) }
    var showDeleteEventDialog by remember { mutableStateOf<com.hault.codex.data.model.Event?>(null) }
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Characters", "Locations", "Timeline")

    CodexTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(worldName) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
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
                    0 -> CharactersList(characters, locations, { showDeleteCharacterDialog = it }, navController, worldId)
                    1 -> LocationsList(locations, navController, worldId) { showDeleteLocationDialog = it }
                    2 -> TimelineList(events, navController, worldId) { showDeleteEventDialog = it }
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

            showDeleteLocationDialog?.let { locationToDelete ->
                AlertDialog(
                    onDismissRequest = { showDeleteLocationDialog = null },
                    title = { Text("Delete Location") },
                    text = { Text("Are you sure you want to delete ${locationToDelete.name}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteLocation(locationToDelete)
                            showDeleteLocationDialog = null
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteLocationDialog = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            showDeleteEventDialog?.let { eventToDelete ->
                AlertDialog(
                    onDismissRequest = { showDeleteEventDialog = null },
                    title = { Text("Delete Event") },
                    text = { Text("Are you sure you want to delete ${eventToDelete.name}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            timelineViewModel.deleteEvent(eventToDelete)
                            showDeleteEventDialog = null
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteEventDialog = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CharactersList(
    characters: List<Character>,
    locations: List<com.hault.codex.data.model.Location>,
    setShowDeleteCharacterDialog: (Character?) -> Unit,
    navController: NavController,
    worldId: Int
) {
    if (characters.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No characters found. Click the '+' button to add one.",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn {
            items(characters) { character ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { navController.navigate("add_character/$worldId?characterId=${character.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            character.homeLocationId?.let { homeLocationId ->
                                val locationName = locations.find { it.id == homeLocationId }?.name
                                if (locationName != null) {
                                    Text(
                                        text = "Home: $locationName",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
}

@Composable
fun LocationsList(
    locations: List<com.hault.codex.data.model.Location>,
    navController: NavController,
    worldId: Int,
    setShowDeleteLocationDialog: (com.hault.codex.data.model.Location?) -> Unit
) {
    if (locations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No locations found. Click the '+' button to add one.",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn {
            items(locations) { location ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { navController.navigate("add_edit_location?worldId=$worldId&locationId=${location.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = location.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { setShowDeleteLocationDialog(location) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Location")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineList(
    events: List<com.hault.codex.data.model.Event>,
    navController: NavController,
    worldId: Int,
    setShowDeleteEventDialog: (com.hault.codex.data.model.Event?) -> Unit
) {
    if (events.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No events found. Click the '+' button to add one.",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn {
            items(events) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { navController.navigate("add_edit_event/$worldId?eventId=${event.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.name,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = event.date,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { setShowDeleteEventDialog(event) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Event")
                        }
                    }
                }
            }
        }
    }
}
