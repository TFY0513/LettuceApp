package com.example.lettuceapp.model

data class Quiz (val answer: String? = null,
                 val options: List<String>? = null
                 ,val question: String? = null
) {
}