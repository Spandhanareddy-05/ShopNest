package com.example.shopnest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shopnest.ui.theme.ShopNestTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShopNestTheme {
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(3000) // Show splash for 3 seconds
                    showSplash = false

                    // Navigate to LoginActivity after splash
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish() // Remove Splash from backstack
                }

                if (showSplash) {
                    SplashScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.backround1), // Replace with your background image
            contentDescription = "Splash Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // App logo
        Image(
            painter = painterResource(id = R.drawable.shopnest), // Replace with your app logo
            contentDescription = "ShopNest Logo",
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.Center)
        )
    }
}
