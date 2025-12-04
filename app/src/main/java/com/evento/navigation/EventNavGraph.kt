package com.evento.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                }, {
                    navController.navigate(Screen.CustomerDetails.route)
                })
            }

            composable(Screen.CustomerDetails.route) {
                CustomerDetailsScreen({
                    navController.navigateUp()
                }, {

                })
            }
        }
    }
}