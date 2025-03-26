package com.example.shopnest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopnest.ui.theme.ShopNestTheme
import androidx.compose.ui.layout.ContentScale

import com.example.shopnest.utils.GDPRPrefs

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopNestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // GDPR state management
    var showGDPRDialog by remember { mutableStateOf(!GDPRPrefs.hasAgreed(context)) }

    // GDPR Consent Dialog
    if (showGDPRDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    GDPRPrefs.setAgreed(context)
                    showGDPRDialog = false
                }) {
                    Text("I Agree")
                }
            },
            title = { Text("Privacy & Data Consent") },
            text = {
                Text(
                    "By using ShopNest, you agree to our Privacy Policy and Terms of Use. " +
                            "We collect and process your personal data in accordance with GDPR for improving your shopping experience."
                )
            }
        )
    }

    // UI as-is...
    Box(modifier = Modifier.fillMaxSize()) {
        // Background and overlay
        Image(
            painter = painterResource(id = R.drawable.backround1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        Toast.makeText(context, "Password recovery not implemented", Toast.LENGTH_SHORT).show()
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, HomeActivity::class.java))
                },
                enabled = !showGDPRDialog,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Don't have an account?", color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, SignupActivity::class.java))
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    ShopNestTheme {
        LoginScreen()
    }
}
