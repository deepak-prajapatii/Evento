package com.evento.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


const val NAV_HOST_ROUTE = "main-route"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EventNavGraph() {
    val navController = rememberAnimatedNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        AnimatedNavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            route = NAV_HOST_ROUTE,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },           // from right
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(250))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },       // slight slide left
                    animationSpec = tween(200)
                ) + fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 2 },      // from left when going back
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(250))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },            // to right when going back
                    animationSpec = tween(200)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) {
            composable(Screen.Home.route) {
                EventBookingScreen(
                    onAddEventClick = {
                        navController.navigate(Screen.SlotSelection.route)
                    }
                )
            }

            composable(Screen.SlotSelection.route) {
                SlotSelectionScreen(
                    onBackClick = { navController.navigateUp() },
                    onContinueClick = { slotId, slotName, startTime, endTime ->
                        navController.navigate(
                            Screen.CustomerDetails.createRoute(
                                slotId, slotName, startTime, endTime
                            )
                        )
                    }
                )
            }

            composable(
                Screen.CustomerDetails.route,
                arguments = listOf(
                    navArgument(AppConstants.SLOT_ID) { type = NavType.StringType },
                    navArgument(AppConstants.SLOT_NAME) { type = NavType.StringType },
                    navArgument(AppConstants.START_TIME) { type = NavType.StringType },
                    navArgument(AppConstants.END_TIME) { type = NavType.StringType },
                )
            ) {
                CustomerDetailsScreen(
                    onBackClick = { navController.navigateUp() },
                    navigateToEventBooking = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}