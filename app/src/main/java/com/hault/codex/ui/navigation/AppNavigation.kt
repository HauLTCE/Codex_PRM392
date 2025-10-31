package com.hault.codex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hault.codex.ui.addeditworld.AddEditWorldScreen
import com.hault.codex.ui.characterlist.CharacterListScreen
import com.hault.codex.ui.addeditcharacter.AddEditCharacterScreen
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
        composable(
            route = "character_list/{worldId}",
            arguments = listOf(navArgument("worldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: 0
            CharacterListScreen(navController = navController)
        }
        composable(
            route = "add_character/{worldId}",
            arguments = listOf(navArgument("worldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: 0
            AddEditCharacterScreen(navController = navController)
        }
    }
}
