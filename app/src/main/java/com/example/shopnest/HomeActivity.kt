package com.example.shopnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shopnest.ui.theme.ShopNestTheme
import com.example.shopnest.data.Product
import com.example.shopnest.ui.theme.ProductCard

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
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> HomeContent()
                1 -> CartContent()
                2 -> ProfileContent()
            }
        }
    }
}

@Composable
fun HomeContent() {
    val allProducts = listOf(
        Product("Red Matte Lipstick", "Makeup", "Flat 30% off - Bestseller"),
        Product("Organic Skincare Combo", "Skincare", "Buy 1 Get 1 Free"),
        Product("Hydrating Face Cream", "Skincare", "Best for dry skin"),
        Product("Floral Summer Dress", "Clothing", "Trending now"),
        Product("Eyeliner Pen", "Makeup", "Smudge-proof & long-lasting"),
        Product("Denim Jacket", "Clothing", "Casual & stylish"),
        Product("Body Lotion", "Skincare", "Deep hydration formula"),
    )

    var selectedCategory by remember { mutableStateOf("All") }

    val filteredProducts = if (selectedCategory == "All") {
        allProducts
    } else {
        allProducts.filter { it.category == selectedCategory }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Welcome to ShopNest!", style = MaterialTheme.typography.headlineMedium)

        // Category filter chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("All", "Skincare", "Makeup", "Clothing").forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }

        Divider()

        Text("Products", style = MaterialTheme.typography.titleMedium)

        // Product list
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            filteredProducts.forEach { product ->
                ProductCard(product)
            }
        }

        Divider()

        Text("Why ShopNest?", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "✓ Curated beauty & fashion just for you\n✓ Affordable luxury\n✓ Fast delivery & easy returns",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun CategoryCard(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
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
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun CartContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your cart is currently empty.", style = MaterialTheme.typography.bodyLarge)
        Text("Start adding your favorite beauty and fashion picks!")
    }
}

@Composable
fun ProfileContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Name: Jane Doe")
        Text("Email: jane@example.com")
        Spacer(modifier = Modifier.height(12.dp))
        Text("Order History, Preferences, and Settings coming soon.")
    }
}
