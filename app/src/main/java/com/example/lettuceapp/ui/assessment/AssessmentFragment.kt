package com.example.lettuceapp.ui.assessment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lettuceapp.R
import com.example.lettuceapp.firebase.AssessmentCallBack
import com.example.lettuceapp.model.Assessment
import com.google.firebase.database.FirebaseDatabase

class AssessmentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_assessment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object{
        fun retrieveAssessment(context: Context, callback : AssessmentCallBack){
            val database = FirebaseDatabase.getInstance()
            val databaseReference =  database.getReference("assessment")

            databaseReference.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onCallBack(it.result.children.count(), it.result.children.mapNotNull { doc ->
                        doc.getValue(Assessment::class.java)
                    })
                } else {
                    Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}