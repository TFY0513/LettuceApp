package com.example.lettuceapp.model

data class Assessment(var date: String? = null, var title: String? = null, var description: String? = null,
                      var duration: String? = null, var questions: List<Question>? = null, var active_timestamp: String?= null,
                      var correctQuestions: Int? = 0, var wrongQuestions: Int? = 0)