package com.example.theweatherapp.fragment.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.navigation.AppNavHost
import com.example.theweatherapp.ui.navigation.NavigationDestination

@Composable
fun MainFragment(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination =
        NavigationDestination.values.find {
            it.route == navBackStackEntry?.destination?.route
        } ?: NavigationDestination.LocalWeather

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
            )
        },
    ) { innerPadding ->
        AppNavHost(navController)
    }
}
