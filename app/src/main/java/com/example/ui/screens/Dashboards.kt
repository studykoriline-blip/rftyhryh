package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun StudentDashboardScreen(navController: NavController, viewModel: AppViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val enrollments by viewModel.getStudentEnrollments().collectAsState(initial = emptyList())
    val courses by viewModel.courses.collectAsState()

    Scaffold(
        topBar = { TopNav(navController, viewModel) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Welcome back, ${user?.name?.split(" ")?.firstOrNull() ?: ""}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("My Courses", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            items(enrollments) { enrollment ->
                val course = courses.find { it.id == enrollment.courseId }
                if (course != null) {
                    val total = course.curriculum.sumOf { it.lessons.size }
                    val completed = enrollment.progress.completedLessonIds.size
                    val pct = if (total > 0) (completed * 100) / total else 0
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(course.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$pct% complete ($completed/$total lessons)", style = MaterialTheme.typography.bodyMedium)
                            LinearProgressIndicator(
                                progress = { pct / 100f },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val firstLesson = course.curriculum.firstOrNull()?.lessons?.firstOrNull()?.id ?: ""
                                Button(onClick = { navController.navigate(Screen.LessonViewer.createRoute(course.id, firstLesson)) }) {
                                    Text("Continue learning")
                                }
                                if (pct == 100 && (course.quiz == null || enrollment.quizResult?.passed == true)) {
                                    OutlinedButton(onClick = { navController.navigate(Screen.Certificate.createRoute(course.id)) }) {
                                        Text("Certificate")
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

@Composable
fun InstructorDashboardScreen(navController: NavController, viewModel: AppViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val courses by viewModel.getInstructorCourses().collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopNav(navController, viewModel) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Welcome back, ${user?.name?.split(" ")?.firstOrNull() ?: ""}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Instructor", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("My Published Courses", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            items(courses) { course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(course.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(course.category, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(if (course.price == 0) "Free" else "৳${course.price}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
