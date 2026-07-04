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
fun QuizViewerScreen(courseId: String, navController: NavController, viewModel: AppViewModel) {
    val courseState = viewModel.getCourse(courseId).collectAsState(initial = null)
    val course = courseState.value
    
    val user by viewModel.currentUser.collectAsState()
    val enrState = if (user != null) viewModel.getStudentEnrollments().collectAsState(initial = emptyList()) else remember { mutableStateOf(emptyList()) }
    val enrollment = enrState.value.find { it.courseId == courseId }

    var currentIndex by remember { mutableStateOf(0) }
    var answers by remember { mutableStateOf(mapOf<String, Int>()) }
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopNav(navController, viewModel) }
    ) { padding ->
        if (course == null || enrollment == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
        } else {
            val quiz = course.quiz
            if (quiz == null) {
                Text("No quiz for this course", modifier = Modifier.padding(padding).padding(24.dp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    item {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("${course.title} / Quiz", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            if (!submitted && enrollment.quizResult == null) {
                                val currentQ = quiz.questions[currentIndex]
                                Text("Question ${currentIndex + 1} of ${quiz.questions.size}", style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(quiz.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(currentQ.text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                currentQ.options.forEachIndexed { index, option ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = answers[currentQ.id] == index,
                                            onClick = { answers = answers + (currentQ.id to index) }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(option)
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(32.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    OutlinedButton(
                                        onClick = { if (currentIndex > 0) currentIndex-- },
                                        enabled = currentIndex > 0
                                    ) {
                                        Text("Previous")
                                    }
                                    Button(
                                        onClick = {
                                            if (currentIndex < quiz.questions.size - 1) {
                                                currentIndex++
                                            } else {
                                                var correct = 0
                                                quiz.questions.forEach { q ->
                                                    if (answers[q.id] == q.correctIndex) correct++
                                                }
                                                val score = (correct * 100) / quiz.questions.size
                                                val passed = score >= quiz.passScore
                                                viewModel.submitQuiz(courseId, score, passed)
                                                submitted = true
                                            }
                                        },
                                        enabled = answers.containsKey(currentQ.id)
                                    ) {
                                        Text(if (currentIndex == quiz.questions.size - 1) "Submit quiz" else "Next question")
                                    }
                                }
                            } else {
                                val result = enrollment.quizResult
                                if (result != null) {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${result.score}%",
                                                style = MaterialTheme.typography.displayMedium,
                                                color = if (result.passed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Text(
                                                if (result.passed) "Great work — you passed!" else "Not quite — try again",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                                OutlinedButton(onClick = { navController.navigate(Screen.LessonViewer.createRoute(course.id, course.curriculum.firstOrNull()?.lessons?.firstOrNull()?.id ?: "")) }) {
                                                    Text("Back to course")
                                                }
                                                if (!result.passed) {
                                                    Button(onClick = {
                                                        // Note: Re-taking logic would clear the quiz result, skipping for simplicity in this demo, just navigating back
                                                        navController.popBackStack()
                                                    }) {
                                                        Text("Retry later")
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
    }
}
