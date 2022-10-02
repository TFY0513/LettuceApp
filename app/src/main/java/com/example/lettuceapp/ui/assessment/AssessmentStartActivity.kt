package com.example.lettuceapp.ui.assessment

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.ActivityAssessmentStartBinding
import com.example.lettuceapp.firebase.AssessmentCallBack
import com.example.lettuceapp.model.Assessment
import com.google.firebase.database.FirebaseDatabase

class AssessmentStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentStartBinding
//    private lateinit var _title: String
//    private lateinit var _date: String
    private lateinit var _ID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

//        _title = intent.getStringExtra("TITLE").toString()
//        this.title = _title;

//        _date = intent.getStringExtra("DATE").toString()

        _ID = intent.getStringExtra("ID").toString()

        binding.textViewDescLabel.paintFlags = binding.textViewDescLabel.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.buttonStart.setOnClickListener{
            //Start new intent
            val intent = Intent(applicationContext, AssessmentAnsweringActivity::class.java)
            intent.putExtra("ID",  _ID)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startActivity(applicationContext, intent, null)
        }
    }

    override fun onStart() {
        super.onStart()

        loadPage(this, object: AssessmentCallBack{
            override fun onCallBack(assessment :Assessment) {
                Log.d("", "Title => ${assessment.title!!}")
                title = assessment.title
                binding.textViewTitle.text = assessment.title
                binding.textViewDuration.text = String.format("%s %s", assessment.duration, getString(
                    R.string.assessment_duration_unit_default))
                val desc = assessment.description
                if(desc!!.isNotEmpty()){
                    binding.textViewDesc.text = assessment.description
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return false;
    }

    private fun loadPage(context: Context, callback : AssessmentCallBack){
        val database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("assessment/${_ID}")

        Log.d("", "ID => ${_ID}")

        databaseReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onCallBack(it.result.getValue(Assessment::class.java)!!)
            } else {
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                Log.e("", it.exception!!.stackTraceToString())
            }
        }
    }

}