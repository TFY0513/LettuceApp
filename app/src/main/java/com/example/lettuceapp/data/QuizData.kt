package com.example.lettuceapp.data

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp


@IgnoreExtraProperties
data class QuizData(val answer: String? = null,
                     val options: List<String>? = null
                    ,val question: String? = null

) {
}

