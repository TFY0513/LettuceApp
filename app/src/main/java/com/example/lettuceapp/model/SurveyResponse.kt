package com.example.lettuceapp.model

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

data class SurveyResponse (var title: String? = null, var score: Float, var mean: Float){
    companion object{
        fun <T : Any> toMap(obj: T): Map<String, Any?> {
            return (obj::class as KClass<T>).memberProperties.associate { prop ->
                prop.name to prop.get(obj)?.let { value ->
                    if (value::class.isData) {
                        toMap(value)
                    } else {
                        value
                    }
                }
            }
        }
    }
}