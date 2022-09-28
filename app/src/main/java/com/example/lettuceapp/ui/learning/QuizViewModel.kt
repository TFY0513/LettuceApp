package com.example.lettuceapp.ui.learning

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lettuceapp.firebase.QuizCallBack
import com.example.lettuceapp.model.Article
import com.example.lettuceapp.model.Quiz
import com.google.firebase.database.FirebaseDatabase

class QuizViewModel(application: Application) :
    AndroidViewModel(application) {

    fun loadArticleList(callback: QuizCallBack) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("quiz")

        databaseReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val quiz = it.result.children.mapNotNull { doc ->
                    doc.getValue(Quiz::class.java)
                }
                callback.onCallBack(quiz)
            } else {
                Toast.makeText(
                    getApplication(),
                    it.exception?.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}



