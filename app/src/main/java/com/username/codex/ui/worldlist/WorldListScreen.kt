package com.username.codex.ui.worldlist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
fun WorldListScreen(
    navController: NavController,
    viewModel: WorldListViewModel = hiltViewModel()
) {
    val worlds by viewModel.worlds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Worlds") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_world") }) {
                Icon(Icons.Default.Add, contentDescription = "Add World")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(worlds) { world ->
                Text(
                    text = world.name,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
