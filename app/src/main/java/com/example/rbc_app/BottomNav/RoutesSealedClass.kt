package com.example.rbc_app.BottomNav

sealed class RoutesSealedClass(val route:String) {
    object Home : RoutesSealedClass("home_route")
    object FreeLance : RoutesSealedClass("Free_Lance")
    object Profile : RoutesSealedClass("profile_route")
//    object Jobs : RoutesSealedClass("jobs_route")
//    object Internships : RoutesSealedClass("internships")
}