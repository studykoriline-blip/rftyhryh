package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.AppViewModel
import com.example.navigation.Screen

@Composable
fun TopNav(navController: NavController, viewModel: AppViewModel) {
    val user by viewModel.currentUser.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "V Verhal",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (user == null) {
                TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                    Text("Log in", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(onClick = { navController.navigate(Screen.Register.route) }) {
                    Text("Sign up", color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                TextButton(onClick = {
                    if (user?.role == "student") navController.navigate(Screen.StudentDashboard.route)
                    else navController.navigate(Screen.InstructorDashboard.route)
                }) {
                    Text("Dashboard", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = { viewModel.logout(); navController.navigate(Screen.Home.route) }) {
                    Text("Logout", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
