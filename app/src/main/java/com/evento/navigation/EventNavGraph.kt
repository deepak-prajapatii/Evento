package com.evento.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.evento.screen.Screen
import com.evento.ui.customerdetails.CustomerDetailsScreen
import com.evento.ui.eventbooking.EventBookingScreen
import com.evento.ui.slotselection.SlotSelectionScreen


const val NAV_HOST_ROUTE = "main-route"

@Composable
fun EventNavGraph() {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            route = NAV_HOST_ROUTE
        ) {
            composable(Screen.Home.route) {
                EventBookingScreen(
                    onAddEventClick = {
                        navController.navigate(Screen.SlotSelection.route)
                    }
                )
            }

            composable(Screen.SlotSelection.route) {
                SlotSelectionScreen({
                    navController.navigateUp()
                }, { slotId, slotName, startTime, endTime ->
                    navController.navigate(Screen.CustomerDetails.createRoute(
                        slotId, slotName, startTime, endTime
                    ))
                })
            }

            composable(Screen.CustomerDetails.route,
                arguments = listOf(
                    navArgument("slotId") { type = NavType.StringType },
                    navArgument("slotName") { type = NavType.StringType },
                    navArgument("startTime") { type = NavType.StringType },
                    navArgument("endTime") { type = NavType.StringType },
                )) {
                CustomerDetailsScreen(onBackClick = {
                    navController.navigateUp()
                },
                    navigateToEventBooking = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    })
            }
        }
    }
}