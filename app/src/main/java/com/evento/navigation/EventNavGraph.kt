package com.evento.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.evento.screen.Screen
import com.evento.ui.customerdetails.CustomerDetailsScreen
import com.evento.ui.eventbooking.EventBookingScreen
import com.evento.ui.slotselection.SlotSelectionScreen
import com.evento.utils.AppConstants


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
                    navArgument(AppConstants.SLOT_ID) { type = NavType.StringType },
                    navArgument(AppConstants.SLOT_NAME) { type = NavType.StringType },
                    navArgument(AppConstants.START_TIME) { type = NavType.StringType },
                    navArgument(AppConstants.END_TIME) { type = NavType.StringType },
                )) {
                CustomerDetailsScreen(onBackClick = {
                    navController.navigateUp()
                },
                    navigateToEventBooking = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    })
            }
        }
    }
}