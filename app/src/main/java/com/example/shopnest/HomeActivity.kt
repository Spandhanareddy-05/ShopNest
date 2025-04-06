package com.example.shopnest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shopnest.data.Product
import com.example.shopnest.ui.theme.ShopNestTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material3.HorizontalDivider

data class CartItem(val product: Product, val quantity: MutableState<Int>)

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
    var selectedTab by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    val cartItems = remember { mutableStateListOf<CartItem>() }
    val context = LocalContext.current

    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = { Text("Privacy Policy") },
            text = { Text("We value your privacy. We don't share your data. GDPR compliant.") },
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
                Text("Menu", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(label = { Text("Profile") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Settings") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Privacy Policy") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }
                    showPrivacyPolicyDialog = true
                })
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
                    0 -> HomeContent(cartItems)
                    1 -> CartContent(cartItems) {
                        context.startActivity(Intent(context, PaymentActivity::class.java))
                    }
                    2 -> PaymentsContent()
                }
            }
        }
    }
}

@Composable
fun HomeContent(cartItems: SnapshotStateList<CartItem>) {
    val allProducts = listOf(
        Product("Red Matte Lipstick", "Makeup", "Flat 30% off - Bestseller", R.drawable.lipstick, 7.99),
        Product("Organic Skincare Combo", "Skincare", "Buy 1 Get 1 Free", R.drawable.skincare_combo, 12.50),
        Product("Hydrating Face Cream", "Skincare", "Best for dry skin", R.drawable.face_cream, 9.75),
        Product("Floral Summer Dress", "Clothing", "Trending now", R.drawable.floral_dress, 24.99),
        Product("Eyeliner Pen", "Makeup", "Smudge-proof & long-lasting", R.drawable.eyeliner, 5.49),
        Product("Denim Jacket", "Clothing", "Casual & stylish", R.drawable.denim_jacket, 32.99),
        Product("Body Lotion", "Skincare", "Deep hydration formula", R.drawable.body_lotion, 6.25)
    )

    var selectedCategory by remember { mutableStateOf("All") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val filtered = if (selectedCategory == "All") allProducts else allProducts.filter { it.category == selectedCategory }

    if (showBottomSheet && selectedProduct != null) {
        AddToCartBottomSheet(
            product = selectedProduct!!,
            onAdd = { product ->
                val existing = cartItems.find { it.product.name == product.name }
                if (existing != null) {
                    existing.quantity.value++
                } else {
                    cartItems.add(CartItem(product, mutableStateOf(1)))
                }
                showBottomSheet = false
            },
            onDismiss = { showBottomSheet = false }
        )
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Welcome to ShopNest!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("All", "Skincare", "Makeup", "Clothing").forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Text("Products", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(filtered) { product ->
            ProductCard(product) {
                selectedProduct = product
                showBottomSheet = true
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text(product.description, style = MaterialTheme.typography.bodySmall)
            Text("£${product.price}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddToCartBottomSheet(product: Product, onAdd: (Product) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(product.name) },
        text = { Text("Add this product to your cart?\nPrice: £${product.price}") },
        confirmButton = {
            TextButton(onClick = { onAdd(product) }) {
                Text("Add to Cart")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CartContent(cartItems: SnapshotStateList<CartItem>, onPayClick: () -> Unit) {
    val subtotal by remember {
        derivedStateOf {
            cartItems.sumOf { it.product.price * it.quantity.value }
        }
    }
    val tax = subtotal * 0.1
    val total = subtotal + tax

    Scaffold(
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Subtotal: £${"%.2f".format(subtotal)}")
                    Text("Tax (10%): £${"%.2f".format(tax)}")
                    Text(
                        "Total: £${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onPayClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Go to Payment")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cartItems, key = { it.product.name }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.product.name, style = MaterialTheme.typography.titleSmall)
                            Text("£${"%.2f".format(item.product.price)} x ${item.quantity.value}")
                            Text(
                                "Total: £${"%.2f".format(item.product.price * item.quantity.value)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row {
                            IconButton(onClick = {
                                if (item.quantity.value > 1) {
                                    item.quantity.value--
                                } else {
                                    cartItems.remove(item)
                                }
                            }) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                            }

                            Text("${item.quantity.value}", style = MaterialTheme.typography.bodyLarge)

                            IconButton(onClick = {
                                item.quantity.value++
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                            }
                        }
                    }
                }
            }
        }
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
        Text("• UPI - linked")
        Text("• Debit/Credit Card - not added")
        Text("• COD - Available")
    }
}
