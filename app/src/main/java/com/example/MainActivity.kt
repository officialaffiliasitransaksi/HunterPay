package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Brush
import com.example.ui.theme.WhiteGradientStart
import com.example.ui.theme.WhiteGradientEnd
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HunterPayViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: HunterPayViewModel = viewModel()
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                // Listen to viewModel event notifications and pop snackbars
                LaunchedEffect(Unit) {
                    viewModel.uiEvent.collectLatest { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                // Check active route to highlight bottom nav
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(WhiteGradientStart, WhiteGradientEnd)
                            )
                        )
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        bottomBar = {
                        // Display navigation pill bar only for foundational screens
                        val tabScreens = listOf(Screen.Home.route, Screen.Bookings.route, Screen.Chat.route, Screen.History.route)
                        if (currentRoute in tabScreens) {
                            NavigationBar(
                                containerColor = Color.White
                            ) {
                                NavigationBarItem(
                                    selected = currentRoute == Screen.Home.route,
                                    onClick = {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                                    label = { Text("Beranda") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = DeepBlue40,
                                        selectedTextColor = DeepBlue40,
                                        indicatorColor = DeepBlue40.copy(alpha = 0.1f)
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Screen.Bookings.route,
                                    onClick = {
                                        navController.navigate(Screen.Bookings.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "Pesanan") },
                                    label = { Text("Pesanan") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = DeepBlue40,
                                        selectedTextColor = DeepBlue40,
                                        indicatorColor = DeepBlue40.copy(alpha = 0.1f)
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Screen.Chat.route,
                                    onClick = {
                                        navController.navigate(Screen.Chat.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.SmartToy, contentDescription = "Tanya PAI") },
                                    label = { Text("Tanya PAI") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = DeepBlue40,
                                        selectedTextColor = DeepBlue40,
                                        indicatorColor = DeepBlue40.copy(alpha = 0.1f)
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Screen.History.route,
                                    onClick = {
                                        navController.navigate(Screen.History.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.History, contentDescription = "Riwayat") },
                                    label = { Text("Riwayat") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = DeepBlue40,
                                        selectedTextColor = DeepBlue40,
                                        indicatorColor = DeepBlue40.copy(alpha = 0.1f)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. HomeScreen Dashboard
                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        // 2. Flights screen
                        composable(Screen.Flights.route) {
                            FlightsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToBookings = {
                                    navController.navigate(Screen.Bookings.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        // 3. Hotels screen
                        composable(Screen.Hotels.route) {
                            HotelsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToBookings = {
                                    navController.navigate(Screen.Bookings.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        // 4. Trains screen
                        composable(Screen.Trains.route) {
                            TrainsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToBookings = {
                                    navController.navigate(Screen.Bookings.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        // 5. Utility Pulsa screen
                        composable(Screen.Pulsa.route) {
                            PulsaScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToHistory = {
                                    navController.navigate(Screen.History.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        // 6. Utility PLN screen
                        composable(Screen.Pln.route) {
                            PlnScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToHistory = {
                                    navController.navigate(Screen.History.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        // 7. Utility BPJS screen
                        composable(Screen.Bpjs.route) {
                            BpjsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToHistory = {
                                    navController.navigate(Screen.History.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        // 8. Bookings collection vouchers
                        composable(Screen.Bookings.route) {
                            BookingsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigate(Screen.Home.route) }
                            )
                        }

                        // 9. History ledger transactions
                        composable(Screen.History.route) {
                            HistoryScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigate(Screen.Home.route) }
                            )
                        }

                        // 10. AI Chat assistant
                        composable(Screen.Chat.route) {
                            ChatScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigate(Screen.Home.route) }
                            )
                        }
                    }
                }
            }
            }
        }
    }
}
