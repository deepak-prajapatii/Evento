package com.evento.screen

sealed class Screen(val route: String, val name: String) {
    object Home : Screen(route = "home", name = "Home")
    object SlotSelection: Screen(route = "slot_selection", name = "Slot Selection")
    object CustomerDetails : Screen(
        route = "customer_details/{slotId}/{slotName}/{startTime}/{endTime}",
        name = "Customer Details"
    ) {
        fun createRoute(
            slotId: String,
            slotName: String,
            startTime: String,
            endTime: String
        ): String =
            "customer_details/$slotId/$slotName/$startTime/$endTime"
    }
}