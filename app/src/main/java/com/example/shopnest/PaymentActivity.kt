package com.example.shopnest

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.shopnest.ui.theme.ShopNestTheme
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemList = intent.getStringArrayListExtra("cartItems") ?: arrayListOf("No items")
        val total = intent.getDoubleExtra("totalPrice", 0.0)

        setContent {
            ShopNestTheme {
                PaymentScreen(itemList, total) {
                    finish() // Close PaymentActivity and return to Cart/History
                }
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(cartItems: List<String>, total: Double, onPaymentComplete: () -> Unit) {
    val context = LocalContext.current as Activity
    val cardNumber = remember { mutableStateOf("") }
    val cardName = remember { mutableStateOf("") }
    val expiry = remember { mutableStateOf("") }
    val cvv = remember { mutableStateOf("") }

    BackHandler {
        context.finish() // Handle hardware back button as well
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = { context.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (cardNumber.value.length == 16 && cardName.value.isNotBlank() &&
                        expiry.value.matches(Regex("""\d{2}/\d{2}""")) && cvv.value.length == 3
                    ) {
                        val orderId = UUID.randomUUID().toString().substring(0, 8).uppercase()
                        val deliveryDate = Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_YEAR, 5)
                        }.time
                        val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(deliveryDate)

                        val itemSummary = cartItems.joinToString("\n") { "• $it" }

                        Toast.makeText(
                            context,
                            """
                            ✅ Order Placed!
                            Order ID: $orderId
                            Total: £${"%.2f".format(total)}
                            
                            📦 Delivery by: $formattedDate

                            Items:
                            $itemSummary
                            """.trimIndent(),
                            Toast.LENGTH_LONG
                        ).show()

                        onPaymentComplete()
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill all card details correctly.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Pay £${"%.2f".format(total)}")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Your Items", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                items(cartItems) { item ->
                    Text(item)
                }
            }

            Divider()

            Text("Card Details", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = cardName.value,
                onValueChange = { cardName.value = it },
                label = { Text("Name on Card") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cardNumber.value,
                onValueChange = { if (it.length <= 16) cardNumber.value = it },
                label = { Text("Card Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = expiry.value,
                    onValueChange = { if (it.length <= 5) expiry.value = it },
                    label = { Text("Expiry (MM/YY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cvv.value,
                    onValueChange = { if (it.length <= 3) cvv.value = it },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
