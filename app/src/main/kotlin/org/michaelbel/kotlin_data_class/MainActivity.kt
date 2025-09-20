@file:OptIn(ExperimentalMaterial3Api::class)

package org.michaelbel.kotlin_data_class

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.michaelbel.kotlin_data_class.ui.AppTheme

class MainActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                var selectedTab by remember { mutableStateOf("Compose") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text(text = "Plurals") }) },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTab == "View",
                                onClick = { selectedTab = "View" },
                                label = { Text("View") },
                                icon = {}
                            )
                            NavigationBarItem(
                                selected = selectedTab == "Compose",
                                onClick = { selectedTab = "Compose" },
                                label = { Text("Compose") },
                                icon = {}
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = selectedTab,
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    ) {
                        composable("Compose") { PluralsComposable() }
                    }
                }
            }
        }
    }
}