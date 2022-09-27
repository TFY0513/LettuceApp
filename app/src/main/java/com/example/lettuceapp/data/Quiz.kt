package com.example.lettuceapp.data

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp


@IgnoreExtraProperties
data class Quiz(val question: String? = null
                ,val answer1: String? = null
                ,val answer2: String? = null
                ,val answer3: String? = null) {
}