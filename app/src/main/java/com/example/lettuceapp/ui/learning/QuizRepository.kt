package com.example.lettuceapp.ui.learning

import android.util.Log
import com.example.lettuceapp.model.Quiz
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizRepository {
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.getReference("quiz")
    init{
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())

                val quizList: List<Quiz> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(Quiz::class.java)!!
                }

                Log.i("Item", quizList.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
