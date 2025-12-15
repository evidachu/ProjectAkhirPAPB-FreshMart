package com.papb.projectakhirandroid.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.navigation.graph.MainNavGraph
import com.papb.projectakhirandroid.presentation.component.BottomBar
import com.papb.projectakhirandroid.ui.theme.DIMENS_16dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_32dp

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit // 1. Menerima callback
) {
    Scaffold(
        bottomBar = {
            Surface(
                elevation = DIMENS_32dp,
                shape = RoundedCornerShape(topStart = DIMENS_16dp, topEnd = DIMENS_16dp)
            ) {
                BottomBar(navController = navController)
            }
        },
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            // 2. Meneruskan callback ke MainNavGraph
            MainNavGraph(
                navController = navController,
                onLogout = onLogout
            )
        }
    }
}