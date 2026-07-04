package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.AppViewModel
import com.example.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController, viewModel: AppViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController, viewModel) }
        composable(Screen.Courses.route) { CoursesScreen(navController, viewModel) }
        composable(Screen.CourseDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            CourseDetailScreen(id, navController, viewModel)
        }
        composable(Screen.Login.route) { LoginScreen(navController, viewModel, isLogin = true) }
        composable(Screen.Register.route) { LoginScreen(navController, viewModel, isLogin = false) }
        composable(Screen.Checkout.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            CheckoutScreen(id, navController, viewModel)
        }
        composable(Screen.StudentDashboard.route) { StudentDashboardScreen(navController, viewModel) }
        composable(Screen.InstructorDashboard.route) { InstructorDashboardScreen(navController, viewModel) }
        composable(Screen.LessonViewer.route) { backStackEntry ->
            val cId = backStackEntry.arguments?.getString("courseId") ?: ""
            val lId = backStackEntry.arguments?.getString("lessonId") ?: ""
            LessonViewerScreen(cId, lId, navController, viewModel)
        }
        composable(Screen.QuizViewer.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            QuizViewerScreen(id, navController, viewModel)
        }
        composable(Screen.Certificate.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            CertificateScreen(id, navController, viewModel)
        }
    }
}
