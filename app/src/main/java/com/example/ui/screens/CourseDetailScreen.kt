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
import com.example.data.DomainCourse
import com.example.navigation.Screen
import com.example.ui.components.TopNav

@Composable
fun CourseDetailScreen(courseId: String, navController: NavController, viewModel: AppViewModel) {
    val courseState = viewModel.getCourse(courseId).collectAsState(initial = null)
    val course = courseState.value

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
                        Text("${course.category} · ${course.level}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(course.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(course.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val price = if (course.price == 0) "Free" else "৳${course.price}"
                                Text(price, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        if (viewModel.currentUser.value == null) {
                                            navController.navigate(Screen.Login.route)
                                        } else if (course.price == 0) {
                                            viewModel.enroll(course.id, "free", 0) {
                                                navController.navigate(Screen.LessonViewer.createRoute(course.id, course.curriculum.firstOrNull()?.lessons?.firstOrNull()?.id ?: ""))
                                            }
                                        } else {
                                            navController.navigate(Screen.Checkout.createRoute(course.id))
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (course.price == 0) "Enroll for free" else "Enroll now")
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Curriculum", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        course.curriculum.forEach { module ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(module.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    module.lessons.forEach { lesson ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val icon = if (lesson.type == "video") "▶" else "📄"
                                            Text("$icon ${lesson.title}", style = MaterialTheme.typography.bodyMedium)
                                            if (lesson.duration != null) {
                                                Text(lesson.duration, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
