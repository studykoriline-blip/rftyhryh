package com.example.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiHelper {
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val moduleListAdapter = moshi.adapter<List<Module>>(
        Types.newParameterizedType(List::class.java, Module::class.java)
    )

    val quizAdapter = moshi.adapter(Quiz::class.java)

    val reviewListAdapter = moshi.adapter<List<Review>>(
        Types.newParameterizedType(List::class.java, Review::class.java)
    )

    val progressAdapter = moshi.adapter(Progress::class.java)
    
    val quizResultAdapter = moshi.adapter(QuizResult::class.java)
}
