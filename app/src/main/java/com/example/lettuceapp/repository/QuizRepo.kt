package com.example.lettuceapp.repository

import com.example.lettuceapp.model.Quiz

interface QuizRepo {

    fun getQuiz(): List<Quiz>
}