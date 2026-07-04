package com.example.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

object InitialData {
    fun seedDatabase(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context)
            val dao = db.appDao()
            
            // Check if already seeded
            if (dao.getUserByEmail("student@verhal.edu") != null) return@launch
            
            val inst1 = "u_inst_1"
            val inst2 = "u_inst_2"
            
            dao.insertUser(User(inst1, "Rafiq Ahmed", "rafiq@verhal.edu", "teach123", "instructor", "2025-11-02"))
            dao.insertUser(User(inst2, "Nusrat Jahan", "nusrat@verhal.edu", "teach123", "instructor", "2025-12-10"))
            dao.insertUser(User("u_stud_1", "Tanvir Hasan", "student@verhal.edu", "learn123", "student", "2026-01-15"))
            
            val courses = listOf(
                Course(
                    id = "c1",
                    title = "Modern Frontend Development with JavaScript",
                    category = "Web Development",
                    level = "Beginner",
                    price = 2400,
                    instructorId = inst1,
                    instructorName = "Rafiq Ahmed",
                    color = "linear-gradient(135deg,#2b3a67,#1b2540)",
                    summary = "Build real interfaces from scratch — HTML, CSS and JavaScript fundamentals through to shipping a live project.",
                    rating = 4.8f,
                    curriculumJson = MoshiHelper.moduleListAdapter.toJson(listOf(
                        Module("m1", "Foundations", listOf(
                            Lesson("l1", "How the web actually works", "video", "8:20"),
                            Lesson("l2", "Setting up your first page", "text", null, "In this lesson we create index.html, link a stylesheet, and open it in the browser.")
                        )),
                        Module("m2", "JavaScript Essentials", listOf(
                            Lesson("l3", "Variables and functions", "video", "12:05"),
                            Lesson("l4", "Working with the DOM", "video", "15:40")
                        ))
                    )),
                    quizJson = MoshiHelper.quizAdapter.toJson(Quiz(
                        id = "q1",
                        title = "Frontend Fundamentals Check",
                        passScore = 70,
                        questions = listOf(
                            Question("qq1", "Which tag is used to link an external stylesheet?", listOf("<style>","<link>","<css>","<script>"), 1),
                            Question("qq2", "Which keyword declares a block-scoped variable in JS?", listOf("var","let","global","define"), 1)
                        )
                    )),
                    reviewsJson = MoshiHelper.reviewListAdapter.toJson(listOf(
                        Review("Mim.", "Clear explanations, practical projects. Worth it.", 5, null),
                        Review("Rakib.", "Good pacing for a beginner like me.", 4, null)
                    ))
                ),
                Course(
                    id = "c3",
                    title = "Digital Marketing for Small Businesses",
                    category = "Marketing",
                    level = "Intermediate",
                    price = 0,
                    instructorId = inst1,
                    instructorName = "Rafiq Ahmed",
                    color = "linear-gradient(135deg,#3a2b4a,#1b2540)",
                    summary = "A free primer on running social and search campaigns on a limited budget.",
                    rating = 4.4f,
                    curriculumJson = MoshiHelper.moduleListAdapter.toJson(listOf(
                        Module("m1", "Getting Started", listOf(
                            Lesson("l1", "Setting a monthly budget", "text", null, "Before spending a single taka on ads..."),
                            Lesson("l2", "Choosing your first channel", "video", "7:30")
                        ))
                    )),
                    quizJson = null,
                    reviewsJson = "[]"
                )
            )
            
            courses.forEach { dao.insertCourse(it) }
        }
    }
}
