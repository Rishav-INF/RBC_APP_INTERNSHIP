package com.example.rbc_app.BottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rbc_app.R

data class BottomNavItems(
    val label : String="",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String=""
)  {
    fun bottomNavigationItems():List<BottomNavItems>{
        return listOf(
            BottomNavItems(
                label = "Home",
                icon = Icons.Filled.Home,
                route = RoutesSealedClass.Home.route
            ),
//            BottomNavItems(
//                label = "Internship",
//                icon = Icons.Filled.Star,
//                route = RoutesSealedClass.Internships.route
//            ),
//            BottomNavItems(
//                label = "Jobs",
//                icon = Icons.Filled.MailOutline,
//                route = RoutesSealedClass.Jobs.route
//            ),
            BottomNavItems(
                label = "Free_Lance",
                icon = Icons.Filled.Search,
                route = RoutesSealedClass.FreeLance.route
            ),
            BottomNavItems(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                route = RoutesSealedClass.Profile.route
            )
        )
    }
}