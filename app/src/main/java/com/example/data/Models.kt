package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String, // "student" or "instructor"
    val joinedDate: String
)

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val level: String,
    val price: Int,
    val instructorId: String,
    val instructorName: String,
    val color: String,
    val summary: String,
    val rating: Float,
    val curriculumJson: String, // JSON serialized List<Module>
    val quizJson: String?, // JSON serialized Quiz?
    val reviewsJson: String // JSON serialized List<Review>
)

@Entity(tableName = "enrollments")
data class Enrollment(
    @PrimaryKey val id: String,
    val userId: String,
    val courseId: String,
    val enrolledAt: String,
    val paymentId: String,
    val progressJson: String, // JSON serialized Progress
    val quizResultJson: String? // JSON serialized QuizResult?
)

@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey val id: String,
    val userId: String,
    val courseId: String,
    val method: String,
    val amount: Int,
    val status: String,
    val date: String
)

@JsonClass(generateAdapter = true)
data class Module(
    val id: String,
    val title: String,
    val lessons: List<Lesson>
)

@JsonClass(generateAdapter = true)
data class Lesson(
    val id: String,
    val title: String,
    val type: String, // "video" or "text"
    val duration: String? = null,
    val content: String? = null
)

@JsonClass(generateAdapter = true)
data class Quiz(
    val id: String,
    val title: String,
    val passScore: Int,
    val questions: List<Question>
)

@JsonClass(generateAdapter = true)
data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

@JsonClass(generateAdapter = true)
data class Review(
    val name: String,
    val text: String,
    val rating: Int,
    val reviewerId: String? = null
)

@JsonClass(generateAdapter = true)
data class Progress(
    val completedLessonIds: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class QuizResult(
    val score: Int,
    val passed: Boolean,
    val attempts: Int,
    val date: String
)
