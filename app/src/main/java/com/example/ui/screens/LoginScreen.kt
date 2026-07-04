package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.AppViewModel
import com.example.navigation.Screen
import com.example.ui.components.TopNav
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, viewModel: AppViewModel, isLogin: Boolean) {
    var email by remember { mutableStateOf(if (isLogin) "student@verhal.edu" else "") }
    var password by remember { mutableStateOf(if (isLogin) "learn123" else "") }
    var name by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("student") }
    var message by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopNav(navController, viewModel) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(if (isLogin) "Welcome back" else "Create account", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (isLogin) "Log in to Verhal" else "Join Verhal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (!isLogin) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = selectedRole == "student",
                                onClick = { selectedRole = "student" },
                                label = { Text("I'm a student") }
                            )
                            FilterChip(
                                selected = selectedRole == "instructor",
                                onClick = { selectedRole = "instructor" },
                                label = { Text("I'm an instructor") }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            if (isLogin) {
                                viewModel.login(email, password) { success, msg ->
                                    if (success) {
                                        if (viewModel.currentUser.value?.role == "student") {
                                            navController.navigate(Screen.StudentDashboard.route) { popUpTo(Screen.Home.route) }
                                        } else {
                                            navController.navigate(Screen.InstructorDashboard.route) { popUpTo(Screen.Home.route) }
                                        }
                                    } else {
                                        scope.launch { snackbarHostState.showSnackbar(msg) }
                                    }
                                }
                            } else {
                                viewModel.register(name, email, password, selectedRole) { success, msg ->
                                    if (success) {
                                        if (selectedRole == "student") {
                                            navController.navigate(Screen.StudentDashboard.route) { popUpTo(Screen.Home.route) }
                                        } else {
                                            navController.navigate(Screen.InstructorDashboard.route) { popUpTo(Screen.Home.route) }
                                        }
                                    } else {
                                        scope.launch { snackbarHostState.showSnackbar(msg) }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isLogin) "Log in" else "Create account")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { 
                            if (isLogin) navController.navigate(Screen.Register.route)
                            else navController.navigate(Screen.Login.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isLogin) "New here? Create an account" else "Already have an account? Log in")
                    }
                }
            }
        }
    }
}
