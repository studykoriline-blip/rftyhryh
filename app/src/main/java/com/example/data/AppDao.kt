package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserFlow(id: String): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUser(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE instructorId = :instructorId")
    fun getCoursesByInstructor(instructorId: String): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    fun getCourseFlow(id: String): Flow<Course?>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    suspend fun getCourse(id: String): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Query("DELETE FROM courses WHERE id = :id")
    suspend fun deleteCourse(id: String)

    @Query("SELECT * FROM enrollments WHERE userId = :userId")
    fun getEnrollmentsByUser(userId: String): Flow<List<Enrollment>>

    @Query("SELECT * FROM enrollments WHERE courseId = :courseId")
    fun getEnrollmentsByCourse(courseId: String): Flow<List<Enrollment>>

    @Query("SELECT * FROM enrollments WHERE userId = :userId AND courseId = :courseId LIMIT 1")
    fun getEnrollmentFlow(userId: String, courseId: String): Flow<Enrollment?>

    @Query("SELECT * FROM enrollments WHERE userId = :userId AND courseId = :courseId LIMIT 1")
    suspend fun getEnrollment(userId: String, courseId: String): Enrollment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnrollment(enrollment: Enrollment)

    @Query("SELECT * FROM payments WHERE userId = :userId")
    fun getPaymentsByUser(userId: String): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE courseId = :courseId")
    fun getPaymentsByCourse(courseId: String): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment)
}
