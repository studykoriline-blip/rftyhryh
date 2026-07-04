package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.AppViewModel
import com.example.R
import com.example.navigation.Screen
import com.example.ui.components.TopNav

@Composable
fun HomeScreen(navController: NavController, viewModel: AppViewModel) {
    val courses by viewModel.courses.collectAsState()
    val featured = courses.take(3)

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
                    Text("Learning platform", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Study on your own schedule, graduate on your own terms.",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Verhal brings courses, quizzes, and progress tracking into one calm, distraction-free place.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = { navController.navigate(Screen.Courses.route) }) {
                            Text("Browse courses")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "Hero banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Catalog", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Featured courses", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
            
            items(featured) { course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    onClick = { navController.navigate(Screen.CourseDetail.createRoute(course.id)) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(course.category, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(course.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(course.instructorName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        val price = if (course.price == 0) "Free" else "৳${course.price}"
                        Text(price, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
