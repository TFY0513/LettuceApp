package com.example.lettuceapp.ui.learning

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.lettuceapp.firebase.QuizCallBack
import com.example.lettuceapp.model.Article
import com.example.lettuceapp.model.Quiz
import com.example.lettuceapp.repository.QuizRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

class QuizViewModel(application: Application) :
    AndroidViewModel(application) {
//class QuizViewModel(val repository: QuizRepo) : ViewModel() {
//
//    private val _quiz = MutableLiveData<List<Quiz>>()
//    val quiz: LiveData<List<Quiz>>
//        get() = _quiz
//
//    fun getQuiz(){
//        _quiz.value = repository.getQuiz()
//
//    }
//}


    fun loadQuizList(callback: QuizCallBack) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("quiz")

        databaseReference.child("question_1").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val quiz = it.result.children.mapNotNull { doc ->
                    doc.getValue(Quiz::class.java)
                }
                callback.onCallBack(quiz)
            } else {

            }
        }
    }
}



