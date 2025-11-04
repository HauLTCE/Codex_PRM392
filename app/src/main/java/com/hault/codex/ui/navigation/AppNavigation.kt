package com.hault.codex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hault.codex.ui.addeditworld.AddEditWorldScreen
import com.hault.codex.ui.addeditcharacter.AddEditCharacterScreen
import com.hault.codex.ui.worldlist.WorldListScreen
import com.hault.codex.ui.worlddetail.WorldDetailScreen
import com.hault.codex.ui.worlddashboard.WorldDashboardScreen
import com.hault.codex.ui.addeditlocation.AddEditLocationScreen

import com.hault.codex.ui.addedit.event.AddEditEventScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "world_list") {
        composable("world_list") {
            WorldListScreen(navController = navController)
        }
        composable("add_edit_world?worldId={worldId}", arguments = listOf(navArgument("worldId") { type = NavType.IntType; defaultValue = -1 })) { backStackEntry ->
            AddEditWorldScreen(navController = navController)
        }
        composable("world_detail/{worldId}", arguments = listOf(navArgument("worldId") { type = NavType.IntType })) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: -1
            WorldDetailScreen(navController = navController)
        }
        composable(
            route = "world_dashboard/{worldId}",
            arguments = listOf(navArgument("worldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: 0
            WorldDashboardScreen(navController = navController)
        }
        composable(
            route = "add_character/{worldId}",
            arguments = listOf(navArgument("worldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: 0
            AddEditCharacterScreen(navController = navController)
        }
        composable(
            route = "add_edit_location?worldId={worldId}&locationId={locationId}",
            arguments = listOf(
                navArgument("worldId") { type = NavType.IntType },
                navArgument("locationId") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: 0
            val locationId = backStackEntry.arguments?.getInt("locationId") ?: -1
            AddEditLocationScreen(navController = navController)
        }
        composable(
            route = "add_edit_event/{worldId}",
            arguments = listOf(navArgument("worldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getInt("worldId") ?: 0
            AddEditEventScreen(navController = navController, worldId = worldId)
        }
    }
}
