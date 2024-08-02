package com.example.theweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.theweatherapp.fragment.main.MainFragment
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheWeatherAppTheme {
                val navController = rememberNavController()
                MainFragment(navController)
            }
        }
    }
}
