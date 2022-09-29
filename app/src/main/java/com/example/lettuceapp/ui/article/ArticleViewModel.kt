package com.example.lettuceapp.ui.article

import android.app.Application
import android.app.SharedElementCallback
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lettuceapp.firebase.ArticleCallBack
import com.example.lettuceapp.model.Article
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ArticleViewModel(application: Application) :
    AndroidViewModel(application){

    fun loadArticleList(callback : ArticleCallBack) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("article")

        databaseReference.orderByChild("timestamp").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val articles = it.result.children.mapNotNull { doc ->
                    doc.getValue(Article::class.java)
                }
                callback.onCallBack(articles)
            } else {
                Toast.makeText(getApplication(), it.exception?.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}