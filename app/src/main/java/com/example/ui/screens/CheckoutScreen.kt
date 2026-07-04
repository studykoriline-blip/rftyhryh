package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.AppViewModel
import com.example.navigation.Screen
import com.example.ui.components.TopNav

@Composable
fun CheckoutScreen(courseId: String, navController: NavController, viewModel: AppViewModel) {
    val courseState = viewModel.getCourse(courseId).collectAsState(initial = null)
    val course = courseState.value
    var selectedMethod by remember { mutableStateOf("bkash") }

    Scaffold(
        topBar = { TopNav(navController, viewModel) }
    ) { padding ->
        if (course == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("${course.title} / Checkout", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Order summary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Course price")
                            Text("৳${course.price}")
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total due", fontWeight = FontWeight.Bold)
                            Text("৳${course.price}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Choose a payment method", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val methods = listOf("bkash" to "bKash", "nagad" to "Nagad", "sslcommerz" to "SSLCommerz", "card" to "Credit / Debit Card")
                        methods.forEach { (id, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                RadioButton(
                                    selected = selectedMethod == id,
                                    onClick = { selectedMethod = id }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(name, modifier = Modifier.padding(top = 12.dp))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                viewModel.enroll(course.id, selectedMethod, course.price) {
                                    navController.navigate(Screen.StudentDashboard.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Pay ৳${course.price}")
                        }
                    }
                }
            }
        }
    }
}
