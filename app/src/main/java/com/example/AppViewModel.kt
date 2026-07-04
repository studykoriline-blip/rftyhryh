package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    val courses: StateFlow<List<DomainCourse>> = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUser = SessionManager.currentUser

    fun getCourse(id: String): Flow<DomainCourse?> = repository.getCourse(id)

    fun login(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null && user.passwordHash == pass) {
                SessionManager.login(user)
                onResult(true, "Welcome back!")
            } else {
                onResult(false, "Incorrect credentials")
            }
        }
    }

    fun register(name: String, email: String, pass: String, role: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val existing = repository.getUserByEmail(email)
            if (existing != null) {
                onResult(false, "Email already in use")
            } else {
                val newUser = User(
                    id = "u_${UUID.randomUUID().toString().take(6)}",
                    name = name,
                    email = email,
                    passwordHash = pass,
                    role = role,
                    joinedDate = java.time.LocalDate.now().toString()
                )
                repository.insertUser(newUser)
                SessionManager.login(newUser)
                onResult(true, "Account created")
            }
        }
    }
    
    fun logout() {
        SessionManager.logout()
    }

    fun enroll(courseId: String, method: String, amount: Int, onComplete: () -> Unit) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val paymentId = "pay_${UUID.randomUUID().toString().take(6)}"
            val payment = Payment(
                id = paymentId,
                userId = user.id,
                courseId = courseId,
                method = method,
                amount = amount,
                status = "paid",
                date = java.time.LocalDate.now().toString()
            )
            repository.insertPayment(payment)
            
            val enrollment = DomainEnrollment(
                id = "enr_${UUID.randomUUID().toString().take(6)}",
                userId = user.id,
                courseId = courseId,
                enrolledAt = java.time.LocalDate.now().toString(),
                paymentId = paymentId,
                progress = Progress(),
                quizResult = null
            )
            repository.insertEnrollment(enrollment)
            onComplete()
        }
    }
    
    fun markLessonComplete(courseId: String, lessonId: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val enr = repository.getEnrollmentSingle(user.id, courseId) ?: return@launch
            if (!enr.progress.completedLessonIds.contains(lessonId)) {
                val newProg = enr.progress.copy(
                    completedLessonIds = enr.progress.completedLessonIds + lessonId
                )
                repository.insertEnrollment(enr.copy(progress = newProg))
            }
        }
    }
    
    fun submitQuiz(courseId: String, score: Int, passed: Boolean) {
         val user = currentUser.value ?: return
         viewModelScope.launch {
             val enr = repository.getEnrollmentSingle(user.id, courseId) ?: return@launch
             val newResult = QuizResult(
                 score = score,
                 passed = passed,
                 attempts = (enr.quizResult?.attempts ?: 0) + 1,
                 date = java.time.LocalDate.now().toString()
             )
             repository.insertEnrollment(enr.copy(quizResult = newResult))
         }
    }
    
    fun addCourseReview(courseId: String, rating: Int, text: String) {
         val user = currentUser.value ?: return
         viewModelScope.launch {
             val c = repository.getCourseSingle(courseId) ?: return@launch
             val newReview = Review(
                 name = user.name,
                 text = text,
                 rating = rating,
                 reviewerId = user.id
             )
             val newReviews = c.reviews + newReview
             val newRating = newReviews.map { it.rating }.average().toFloat()
             repository.insertCourse(c.copy(reviews = newReviews, rating = newRating))
         }
    }

    fun getStudentEnrollments(): Flow<List<DomainEnrollment>> {
        val user = currentUser.value ?: return flowOf(emptyList())
        return repository.getEnrollmentsByUser(user.id)
    }

    fun getInstructorCourses(): Flow<List<DomainCourse>> {
        val user = currentUser.value ?: return flowOf(emptyList())
        return repository.getCoursesByInstructor(user.id)
    }
}
