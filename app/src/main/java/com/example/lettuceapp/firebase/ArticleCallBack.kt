package com.example.lettuceapp.firebase

import com.example.lettuceapp.model.Article

interface ArticleCallBack {
    fun onCallBack(value: List<Article>)
}