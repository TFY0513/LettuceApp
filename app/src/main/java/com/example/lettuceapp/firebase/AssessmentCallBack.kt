package com.example.lettuceapp.firebase

import com.example.lettuceapp.model.Assessment

interface AssessmentCallBack {

    /**
     * Implementation expecting to return a list of Assessment with number of rows returned
     */
    fun onCallBack(count: Int, assessmentList: List<Assessment>){
        //Implementation code
    }

    /**
     * Implementation expecting for a single result return
     */
    fun onCallBack(assessment: Assessment){
        //Implementation code
    }

    /**
     * Implementation expecting for assessment id in callback, found as key from database key
     */
    fun onCallBack(id: List<String>, count: Int, assessmentList: List<Assessment>){
        //Implementation code
    }
}