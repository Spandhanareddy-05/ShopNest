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
import kotlinx.coroutines.launch


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    // Show Privacy Policy Dialog
    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = { Text("Privacy Policy") },
            text = {
                Text(
                    "ShopNest respects your privacy. We collect data to enhance your experience, but never sell or misuse it. By using the app, you agree to our use of essential cookies and secure processing of your personal data as per GDPR compliance."
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyPolicyDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(label = { Text("Profile") }, selected = false, onClick = { /* TODO */ })
                NavigationDrawerItem(label = { Text("Settings") }, selected = false, onClick = { /* TODO */ })
                NavigationDrawerItem(
                    label = { Text("Privacy Policy") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showPrivacyPolicyDialog = true
                    }
                )
                NavigationDrawerItem(label = { Text("About") }, selected = false, onClick = { /* TODO */ })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ShopNest") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
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
                        icon = { Icon(Icons.Default.Payments, contentDescription = "Payments") },
                        label = { Text("Payments") }
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
                    2 -> PaymentsContent()
                }
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
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(product.name, style = MaterialTheme.typography.bodyLarge)
            Text(product.description, style = MaterialTheme.typography.bodySmall)
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
fun PaymentsContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Payment Methods", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("• UPI - linked", style = MaterialTheme.typography.bodyLarge)
        Text("• Debit/Credit Card - not added", style = MaterialTheme.typography.bodyLarge)
        Text("• COD - Available", style = MaterialTheme.typography.bodyLarge)
    }
}

