package com.example.lettuceapp.data

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class ArticleData(val article_url: String? = null
    ,val author: String? = null
    ,val image_preview_url: String? = null
    ,val summary: String? = null
    ,val timestamp: Timestamp? = null
    ,val title: String? = null) {
}