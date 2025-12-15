package com.papb.projectakhirandroid.presentation.screen.about

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.common.card.ProfileCard
import com.papb.projectakhirandroid.presentation.common.content.ListContentAbout
import com.papb.projectakhirandroid.ui.theme.*

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navController: NavController, // Masih dibutuhkan untuk Edit Profile
    profileViewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogout: () -> Unit = { authViewModel.logout() }
)
 {
    val activity = LocalContext.current as Activity
    val name by profileViewModel.name.collectAsState()
    val email by profileViewModel.email.collectAsState()
    val imageUri by profileViewModel.profileImageUri.collectAsState()

    Column(
        modifier = modifier
            .padding(top = DIMENS_24dp)
            .fillMaxSize()
    ) {
        ProfileCard(
            name = name,
            email = email,
            imageUri = imageUri
        )

        Button(
            onClick = { navController.navigate(Screen.EditProfile.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DIMENS_16dp, vertical = DIMENS_8dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(
                text = "Edit Profile",
                color = Color.Black,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }

        ListContentAbout(onItemClick = {
            if (it == "Koleksi") {
                navController.navigate("collection_graph")
            }
        })

        Spacer(modifier = Modifier.height(DIMENS_32dp))

        ButtonLogout {
            onLogout()
        }
    }
}

@Composable
fun ButtonLogout(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(DIMENS_16dp)
            .height(DIMENS_48dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(DIMENS_16dp),
        elevation = ButtonDefaults.elevation(DIMENS_2dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = GrayBackground),
        onClick = { onClick.invoke() }
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = stringResource(R.string.logout),
            tint = Green
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.logout),
            color = Green,
            textAlign = TextAlign.Center,
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    // This preview will not show real data, but it will prevent the crash.
    Column(
        modifier = Modifier
            .padding(top = DIMENS_24dp)
            .fillMaxSize()
    ) {
        ProfileCard(name = "John Doe", email = "johndoe@example.com", imageUri = null)

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DIMENS_16dp, vertical = DIMENS_8dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(
                text = "Edit Profile",
                color = Color.Black,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }

        ListContentAbout(onItemClick = {})

        Spacer(modifier = Modifier.height(DIMENS_32dp))

        ButtonLogout { }
    }
}
