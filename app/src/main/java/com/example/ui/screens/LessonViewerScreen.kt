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
fun LessonViewerScreen(courseId: String, lessonId: String, navController: NavController, viewModel: AppViewModel) {
    val courseState = viewModel.getCourse(courseId).collectAsState(initial = null)
    val course = courseState.value
    
    val user by viewModel.currentUser.collectAsState()
    val enrState = if (user != null) viewModel.getStudentEnrollments().collectAsState(initial = emptyList()) else remember { mutableStateOf(emptyList()) }
    val enrollment = enrState.value.find { it.courseId == courseId }

    Scaffold(
        topBar = { TopNav(navController, viewModel) }
    ) { padding ->
        if (course == null || enrollment == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
        } else {
            val allLessons = course.curriculum.flatMap { it.lessons }
            val current = allLessons.find { it.id == lessonId } ?: allLessons.firstOrNull()
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("${course.title} / Lesson", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                if (current?.type == "video") {
                                    Text("▶ Video player placeholder (${current.duration})")
                                } else {
                                    Text("📄 Reading lesson")
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(current?.title ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (current?.type == "text") {
                            Text(current.content ?: "", style = MaterialTheme.typography.bodyLarge)
                        } else {
                            Text("Video content plays above.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        val isComplete = enrollment.progress.completedLessonIds.contains(current?.id)
                        Button(
                            onClick = { 
                                current?.id?.let { viewModel.markLessonComplete(courseId, it) } 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isComplete
                        ) {
                            Text(if (isComplete) "Marked complete ✓" else "Mark as complete")
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Course content", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                
                items(allLessons) { lesson ->
                    val isDone = enrollment.progress.completedLessonIds.contains(lesson.id)
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (lesson.id == current?.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = { navController.navigate(Screen.LessonViewer.createRoute(courseId, lesson.id)) }
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(if (isDone) "✓ " else "○ ", color = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(lesson.title, color = if (lesson.id == current?.id) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                
                if (course.quiz != null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { navController.navigate(Screen.QuizViewer.createRoute(course.id)) },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                        ) {
                            Text("Take module quiz")
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
