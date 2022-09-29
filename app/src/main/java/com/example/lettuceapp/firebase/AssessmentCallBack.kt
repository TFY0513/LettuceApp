package com.example.lettuceapp.firebase

import com.example.lettuceapp.model.Assessment

interface AssessmentCallBack {
    fun onCallBack(count: Int, assessmentList: List<Assessment>)
}