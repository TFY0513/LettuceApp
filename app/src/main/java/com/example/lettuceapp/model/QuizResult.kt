package com.example.lettuceapp.model

import java.sql.Timestamp

data class QuizResult (
    var quizID: String? = null,
    var userID: String? = null,
    var date: String? = null,
    var correctQues: Int? = null,
    var skipQues: Int? = null,
    var course: String? = null,
    var totalQues: Int? = null,
    var quiz_level: String? = null
)