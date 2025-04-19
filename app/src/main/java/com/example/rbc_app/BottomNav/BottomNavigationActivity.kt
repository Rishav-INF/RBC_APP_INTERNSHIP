package com.example.rbc_app.BottomNav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rbc_app.BottomNav.Screens.CareerOpportunitiesScreen
import com.example.rbc_app.BottomNav.Screens.FreeLanceScreen
import com.example.rbc_app.BottomNav.Screens.InternshipsScreen
import com.example.rbc_app.BottomNav.Screens.JobsScreen
import com.example.rbc_app.BottomNav.Screens.ProfileScreen

class BottomNavigationActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(){
            BottomNavBar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(){
    var navigationSelectedItem by remember{
        mutableStateOf(0)
    }
    val navController = rememberNavController()
    androidx.compose.material3.Scaffold (
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                    BottomNavItems().bottomNavigationItems().forEachIndexed{
                        index, bottomNavItems ->
                    NavigationBarItem(
                        /*If our current index of the list of items
     *is equal to navigationSelectedItem then simply
     *The selected item is active in overView this
     *is used to know the selected item
     */
                        selected = index == navigationSelectedItem,

                        //Label is used to bottom navigation labels like Home, Search
                        label = {
                            Text(bottomNavItems.label)
                        },

                        // Icon is used to display the icons of the bottom Navigation Bar
                        icon = {
                            Icon(
                                bottomNavItems.icon,
                                contentDescription = bottomNavItems.label)
                        },
                        // used to handle click events of navigation items
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(bottomNavItems.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    }
            }
        }
    ){
            paddingValues ->
        NavHost(
            navController = navController,
            startDestination = RoutesSealedClass.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues)) {
            composable(RoutesSealedClass.Home.route) {
               CareerOpportunitiesScreen(navController) //call our composable screens here
            }
            composable(RoutesSealedClass.FreeLance.route) {
                FreeLanceScreen(navController) //call our composable screens here
            }
            composable(RoutesSealedClass.Profile.route) {
                ProfileScreen(navController)//call our composable screens here
            }
//            composable(RoutesSealedClass.Jobs.route) {
//                JobsScreen(navController)//call our composable screens here
//            }
//            composable(RoutesSealedClass.Internships.route) {
//                InternshipsScreen(navController)//call our composable screens here
//            }
        }
    }



}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    BottomNavBar()
}