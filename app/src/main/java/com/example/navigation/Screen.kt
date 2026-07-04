package com.example.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Courses : Screen("courses")
    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: String) = "course_detail/$courseId"
    }
    object Login : Screen("login")
    object Register : Screen("register")
    object Checkout : Screen("checkout/{courseId}") {
        fun createRoute(courseId: String) = "checkout/$courseId"
    }
    object StudentDashboard : Screen("student_dashboard")
    object InstructorDashboard : Screen("instructor_dashboard")
    object LessonViewer : Screen("lesson/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String) = "lesson/$courseId/$lessonId"
    }
    object QuizViewer : Screen("quiz/{courseId}") {
        fun createRoute(courseId: String) = "quiz/$courseId"
    }
    object Certificate : Screen("certificate/{courseId}") {
        fun createRoute(courseId: String) = "certificate/$courseId"
    }
}
