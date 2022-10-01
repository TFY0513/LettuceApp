package com.example.lettuceapp.ui.assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lettuceapp.databinding.ActivityAssessmentCompletedResultBinding

class AssessmentCompletedResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentCompletedResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentCompletedResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return false;
    }

    private fun loadPage(){

    }
}