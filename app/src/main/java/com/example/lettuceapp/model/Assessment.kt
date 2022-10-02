package com.example.lettuceapp.model

data class Assessment(var date: String? = null, var title: String? = null, var description: String? = null,
                      var duration: String? = null, var questions: List<Question>? = null, var active_timestamp: String?= null,
                      var correct_questions: Int? = 0, var wrong_questions: Int? = 0, var skipped_questions: Int? = 0,
                      var time_elapsed: String? = "", var completion_date_time: String? = null)