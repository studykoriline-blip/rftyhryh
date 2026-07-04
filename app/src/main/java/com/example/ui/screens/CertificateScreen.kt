package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.AppViewModel
import com.example.navigation.Screen
import com.example.ui.components.TopNav

@Composable
fun CertificateScreen(courseId: String, navController: NavController, viewModel: AppViewModel) {
    val courseState = viewModel.getCourse(courseId).collectAsState(initial = null)
    val course = courseState.value
    val user by viewModel.currentUser.collectAsState()
    
    Scaffold(
        topBar = { TopNav(navController, viewModel) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Certificate of Completion", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (course != null && user != null) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                            ) {
                                Text(user?.name ?: "", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("has successfully completed the course", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(course.title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(32.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                        Text("Instructor", style = MaterialTheme.typography.labelSmall)
                                        Text(course.instructorName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    }
                                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                        Text("Date", style = MaterialTheme.typography.labelSmall)
                                        Text(java.time.LocalDate.now().toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { navController.navigate(Screen.StudentDashboard.route) }) {
                        Text("Back to My Courses")
                    }
                }
            }
        }
    }
}
