package com.example.shopnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shopnest.ui.theme.ShopNestTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopNestTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = { selectedItem = 1 },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") }
                )
                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = { selectedItem = 2 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                0 -> HomeContent()
                1 -> CartContent()
                2 -> ProfileContent()
            }
        }
    }
}

@Composable
fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Welcome to ShopNest!",
            style = MaterialTheme.typography.headlineMedium
        )

        Divider()

        Text(
            text = "Featured Categories",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryCard("Electronics")
            CategoryCard("Fashion")
            CategoryCard("Groceries")
        }

        Divider()

        Text(
            text = "Trending Deals",
            style = MaterialTheme.typography.titleMedium
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DealCard("50% Off on Headphones", "Limited time offer")
            DealCard("Buy 1 Get 1 Free - T-Shirts", "Only today!")
            DealCard("Fresh Fruits 30% Off", "Shop now")
        }
    }
}

@Composable
fun CategoryCard(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Composable
fun DealCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun CartContent() {
    Text(
        text = "Your cart is empty.",
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun ProfileContent() {
    Text(
        text = "User Profile",
        style = MaterialTheme.typography.headlineSmall
    )
}
