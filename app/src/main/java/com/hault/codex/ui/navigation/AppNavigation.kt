package com.hault.codex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hault.codex.ui.addeditworld.AddEditWorldScreen
import com.hault.codex.ui.worldlist.WorldListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "world_list") {
        composable("world_list") {
            WorldListScreen(navController = navController)
        }
        composable("add_world") {
            AddEditWorldScreen(navController = navController)
        }
    }
}
