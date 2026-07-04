package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class DomainCourse(
    val id: String,
    val title: String,
    val category: String,
    val level: String,
    val price: Int,
    val instructorId: String,
    val instructorName: String,
    val color: String,
    val summary: String,
    val rating: Float,
    val curriculum: List<Module>,
    val quiz: Quiz?,
    val reviews: List<Review>
)

data class DomainEnrollment(
    val id: String,
    val userId: String,
    val courseId: String,
    val enrolledAt: String,
    val paymentId: String,
    val progress: Progress,
    val quizResult: QuizResult?
)

fun Course.toDomain(): DomainCourse = DomainCourse(
    id = id,
    title = title,
    category = category,
    level = level,
    price = price,
    instructorId = instructorId,
    instructorName = instructorName,
    color = color,
    summary = summary,
    rating = rating,
    curriculum = MoshiHelper.moduleListAdapter.fromJson(curriculumJson) ?: emptyList(),
    quiz = quizJson?.let { MoshiHelper.quizAdapter.fromJson(it) },
    reviews = MoshiHelper.reviewListAdapter.fromJson(reviewsJson) ?: emptyList()
)

fun DomainCourse.toEntity(): Course = Course(
    id = id,
    title = title,
    category = category,
    level = level,
    price = price,
    instructorId = instructorId,
    instructorName = instructorName,
    color = color,
    summary = summary,
    rating = rating,
    curriculumJson = MoshiHelper.moduleListAdapter.toJson(curriculum),
    quizJson = quiz?.let { MoshiHelper.quizAdapter.toJson(it) },
    reviewsJson = MoshiHelper.reviewListAdapter.toJson(reviews)
)

fun Enrollment.toDomain(): DomainEnrollment = DomainEnrollment(
    id = id,
    userId = userId,
    courseId = courseId,
    enrolledAt = enrolledAt,
    paymentId = paymentId,
    progress = MoshiHelper.progressAdapter.fromJson(progressJson) ?: Progress(),
    quizResult = quizResultJson?.let { MoshiHelper.quizResultAdapter.fromJson(it) }
)

fun DomainEnrollment.toEntity(): Enrollment = Enrollment(
    id = id,
    userId = userId,
    courseId = courseId,
    enrolledAt = enrolledAt,
    paymentId = paymentId,
    progressJson = MoshiHelper.progressAdapter.toJson(progress),
    quizResultJson = quizResult?.let { MoshiHelper.quizResultAdapter.toJson(it) }
)

class AppRepository(private val dao: AppDao) {
    fun getAllCourses(): Flow<List<DomainCourse>> =
        dao.getAllCourses().map { it.map { c -> c.toDomain() } }

    fun getCourse(id: String): Flow<DomainCourse?> =
        dao.getCourseFlow(id).map { it?.toDomain() }

    suspend fun getCourseSingle(id: String): DomainCourse? =
        dao.getCourse(id)?.toDomain()

    suspend fun insertCourse(course: DomainCourse) {
        dao.insertCourse(course.toEntity())
    }

    suspend fun deleteCourse(id: String) {
        dao.deleteCourse(id)
    }
    
    fun getCoursesByInstructor(instructorId: String): Flow<List<DomainCourse>> =
        dao.getCoursesByInstructor(instructorId).map { it.map { c -> c.toDomain() } }

    fun getUser(id: String): Flow<User?> = dao.getUserFlow(id)

    suspend fun getUserSingle(id: String): User? = dao.getUser(id)

    suspend fun getUserByEmail(email: String): User? = dao.getUserByEmail(email)

    suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    fun getEnrollment(userId: String, courseId: String): Flow<DomainEnrollment?> =
        dao.getEnrollmentFlow(userId, courseId).map { it?.toDomain() }

    suspend fun getEnrollmentSingle(userId: String, courseId: String): DomainEnrollment? =
        dao.getEnrollment(userId, courseId)?.toDomain()

    fun getEnrollmentsByUser(userId: String): Flow<List<DomainEnrollment>> =
        dao.getEnrollmentsByUser(userId).map { it.map { e -> e.toDomain() } }
        
    fun getEnrollmentsByCourse(courseId: String): Flow<List<DomainEnrollment>> =
        dao.getEnrollmentsByCourse(courseId).map { it.map { e -> e.toDomain() } }

    suspend fun insertEnrollment(enrollment: DomainEnrollment) {
        dao.insertEnrollment(enrollment.toEntity())
    }
    
    fun getPaymentsByUser(userId: String): Flow<List<Payment>> =
        dao.getPaymentsByUser(userId)
        
    fun getPaymentsByCourse(courseId: String): Flow<List<Payment>> =
        dao.getPaymentsByCourse(courseId)

    suspend fun insertPayment(payment: Payment) {
        dao.insertPayment(payment)
    }
}
