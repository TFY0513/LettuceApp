package com.example.lettuceapp.model

import java.sql.Timestamp

data class Assessment(var date: String? = null, var title: String? = null, var description: String? = null,
                      var duration: String? = null, var questions: List<Question>? = null, var active_timestamp: String?= null)