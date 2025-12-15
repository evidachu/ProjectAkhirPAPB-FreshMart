package com.papb.projectakhirandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.navigation.graph.RootNavigationGraph
import com.papb.projectakhirandroid.presentation.auth.AuthViewModel
import com.papb.projectakhirandroid.ui.theme.GroceriesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GroceriesAppTheme {

                val navController = rememberNavController()
                val uiState by authViewModel.uiState.collectAsState()

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    RootNavigationGraph(
                        navController = navController,
                        isLoggedIn = uiState.isLoggedIn,
                        onLogout = authViewModel::logout
                    )
                }
            }
        }
    }
}
