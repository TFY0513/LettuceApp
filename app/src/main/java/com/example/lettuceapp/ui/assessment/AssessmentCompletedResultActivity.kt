package com.example.lettuceapp.ui.assessment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lettuceapp.MainActivity
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.ActivityAssessmentCompletedResultBinding
import com.example.lettuceapp.model.Assessment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class AssessmentCompletedResultActivity : AppCompatActivity() {
    private val TAG = "AssessmentCompResult"
    private lateinit var binding: ActivityAssessmentCompletedResultBinding

    private lateinit var _ID: String
    private lateinit var _TITLE: String
    private lateinit var _COMPLETION_DATE: String
    private lateinit var _TIME_ELAPSED: String
    private var _SKIPPED: Int = 0
    private var _CORRECT: Int = 0
    private var _WRONG: Int = 0
    private var _TOTAL: Int = 0
    private var hasIntent: Boolean = false

    private lateinit var userId: String

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentCompletedResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        _ID = intent.getStringExtra("ID").toString()
        _COMPLETION_DATE = intent.getStringExtra("COMPLETION_DATE").toString()
        _TIME_ELAPSED = intent.getStringExtra("TIME_ELAPSED").toString()
        _SKIPPED = intent.getIntExtra("SKIPPED", 0)
        _WRONG = intent.getIntExtra("WRONG", 0)
        _CORRECT = intent.getIntExtra("CORRECT", 0)
        _TOTAL = intent.getIntExtra("TOTAL", 0)

        hasIntent = (_SKIPPED!=0&&_CORRECT!=0&&_TOTAL!=0)

        userId = FirebaseAuth.getInstance().currentUser?.uid!!

        binding.buttonBackHome.setOnClickListener{
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
            )
            finish()
        }

        loadPage()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return false;
    }

    private fun loadPage(){
        database = FirebaseDatabase.getInstance()
        var result: Assessment
        if(hasIntent){
            database.getReference("assessment").child(_ID).get().addOnSuccessListener {
                if (it.exists()) {
                    Log.d(TAG, "Loaded with ${it.children.count()} records")
                    Log.d(TAG, "Result => ${it.value}")

                    result = it.getValue(Assessment::class.java)!!

                    _TITLE = result.title!!

                    binding.textViewTitleAssessmentComplete.text = _TITLE
                    binding.textViewAssessmentCompleteTimeElapsed.text = _TIME_ELAPSED
                    binding.textViewAssessmentCompleteDate.text = _COMPLETION_DATE

                    renderPieChart(_CORRECT, _WRONG, _SKIPPED, _TOTAL)
                }
            }
        }else{
            database.getReference("assessment/completed/users").child(userId).child(_ID).get().addOnSuccessListener {
                if (it.exists()) {
                    Log.d(TAG, "Loaded with ${it.children.count()} records")
                    Log.d(TAG, "Result => ${it.value}")

                    result = it.getValue(Assessment::class.java)!!

                    _TITLE = result.title!!
                    _TIME_ELAPSED = result.time_elapsed!!
                    _COMPLETION_DATE = result.completion_date_time!!

                    _WRONG = result.wrong_questions!!
                    _SKIPPED = result.skipped_questions!!
                    _CORRECT = result.correct_questions!!
                    _TOTAL = _WRONG + _SKIPPED + _CORRECT

                    binding.textViewTitleAssessmentComplete.text = _TITLE
                    binding.textViewAssessmentCompleteTimeElapsed.text = _TIME_ELAPSED
                    binding.textViewAssessmentCompleteDate.text = _COMPLETION_DATE

                    renderPieChart(_CORRECT, _WRONG, _SKIPPED, _TOTAL)
                }
            }
        }
    }

    private fun renderPieChart(correct: Int, wrong:Int, skipped: Int, total: Int){
        val pieChart = binding.pieChart

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()

        Log.d(TAG, "Correct => " + correct.toString())
        Log.d(TAG, "skipped => " + skipped.toString())
        Log.d(TAG, "wrong => " + wrong.toString())

        Log.d(TAG, "Correct percent => " + ((correct.toFloat()/total.toFloat()) * 100f).toString())
        Log.d(TAG, "Skipped percent => " + ((skipped.toFloat()/total.toFloat()) * 100f).toString())
        Log.d(TAG, "Wrong percent => " + ((wrong.toFloat()/total.toFloat()) * 100f).toString())
        entries.add(PieEntry(((correct.toFloat()/total.toFloat()) * 100f)))
        entries.add(PieEntry(((skipped.toFloat()/total.toFloat()) * 100f)))
        entries.add(PieEntry(((wrong.toFloat()/total.toFloat()) * 100f)))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Assessment result")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.green))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }
}