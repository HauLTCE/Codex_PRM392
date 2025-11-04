package com.hault.codex.ui.worldlist

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import com.hault.codex.data.model.World
import androidx.compose.material3.IconButton
import com.hault.codex.ui.theme.CodexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldListScreen(
    navController: NavController,
    viewModel: WorldListViewModel = hiltViewModel()
) {
    val worlds by viewModel.worlds.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<World?>(null) }

    CodexTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My Worlds") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("add_edit_world?worldId=-1") }) {
                    Icon(Icons.Default.Add, contentDescription = "Add World")
                }
            }
        ) { paddingValues ->
            if (worlds.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No worlds yet. Tap the '+' button to create your first one!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(worlds) { world ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    navController.navigate("world_dashboard/${world.id}")
                                },
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
                                        text = world.name,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    world.description?.let {
                                        if (it.isNotBlank()) {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                IconButton(onClick = { showDeleteDialog = world }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete World")
                                }
                            }
                        }
                    }
                }
            }

            showDeleteDialog?.let { worldToDelete ->
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = null },
                    title = { Text("Delete World") },
                    text = { Text("Are you sure you want to delete this world? This action cannot be undone.") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteWorld(worldToDelete)
                            showDeleteDialog = null
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
