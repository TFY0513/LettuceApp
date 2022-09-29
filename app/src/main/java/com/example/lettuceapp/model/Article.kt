package com.example.lettuceapp.model

import android.graphics.Bitmap
import java.util.*

data class Article (var article_url : String? = null, var author : String? = null,
               var image_preview_url : String? = null, var summary : String? = null,
               var timestamp: Long? = null, var title: String? = null) {
}