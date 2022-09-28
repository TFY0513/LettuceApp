package com.example.lettuceapp.firebase

import com.example.lettuceapp.model.Quiz

interface QuizCallBack {
    fun onCallBack(value: List<Quiz>)
}
