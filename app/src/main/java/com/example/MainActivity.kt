package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.AppViewModel
import com.example.ui.AppViewModelFactory
import com.example.ui.screens.AddNewsJobEventScreen
import com.example.ui.screens.AddShopScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ShopDetailScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // 1. Initialize DB and Repository layers securely
    val database = AppDatabase.getInstance(this)
    val repository = AppRepository(database)

    // 2. Instantiate ViewModel with factory
    val viewModel = ViewModelProvider(
      this, 
      AppViewModelFactory(repository)
    )[AppViewModel::class.java]

    setContent {
      val isDark by viewModel.isDarkMode.collectAsState()

      // Apply theme according to app's dynamic toggle state, disabling dynamic dynamicColors override
      MyApplicationTheme(darkTheme = isDark, dynamicColor = false) {
        val navController = rememberNavController()

        NavHost(
          navController = navController,
          startDestination = "dashboard",
          modifier = Modifier.fillMaxSize()
        ) {
          // A. Master Dashboard (Tabs for Directory, News, Jobs, Events, Settings)
          composable("dashboard") {
            DashboardScreen(
              viewModel = viewModel,
              onNavigateToShopDetail = { shopId ->
                navController.navigate("shop_detail/$shopId")
              },
              onNavigateToAddShop = {
                navController.navigate("add_shop")
              },
              onNavigateToAddNewsJobsEvents = { type ->
                navController.navigate("add_news_job_event/$type")
              }
            )
          }

          // B. Shop details panel (Products, directions, call/message shortcuts, reviews board)
          composable("shop_detail/{shopId}") { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId")?.toIntOrNull() ?: 1
            ShopDetailScreen(
              shopId = shopId,
              viewModel = viewModel,
              onNavigateBack = { navController.popBackStack() }
            )
          }

          // C. Business Owners dynamic Registration Page
          composable("add_shop") {
            AddShopScreen(
              viewModel = viewModel,
              onNavigateBack = { navController.popBackStack() }
            )
          }

          // D. Admin Alerts Posting Panel (Jobs, news, events)
          composable("add_news_job_event/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "News"
            AddNewsJobEventScreen(
              type = type,
              viewModel = viewModel,
              onNavigateBack = { navController.popBackStack() }
            )
          }
        }
      }
    }
  }
}

