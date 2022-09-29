package com.example.lettuceapp.firebase

import com.example.lettuceapp.model.Survey

interface SurveyCallback {
    fun onCallBack(title: String, value: List<Survey>)

    fun onCallBack(responded: Boolean)
}