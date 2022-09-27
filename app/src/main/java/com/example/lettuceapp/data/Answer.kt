package com.example.lettuceapp.data

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Answer(val answer1: Int? = null
             ,val answer2: Int? = null
             ,val answer3: Int? = null) {
}