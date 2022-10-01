package com.example.lettuceapp.ui.assessment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.ActivityAssessmentAnsweringBinding
import com.example.lettuceapp.firebase.AssessmentCallBack
import com.example.lettuceapp.model.Assessment
import com.example.lettuceapp.model.Question
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_assessment_answering.view.*
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class AssessmentAnsweringActivity : AppCompatActivity() {

    private val TAG = "AssessmentAnsweringAct"
    private lateinit var binding: ActivityAssessmentAnsweringBinding

    private lateinit var _ID: String

    private lateinit var timer : Timer
    private lateinit var timerTask : TimerTask
    private var time : Double = 0.0

    //for retrieving question
    private var position: Int = 0
    private lateinit var database: DatabaseReference
    private var correctQues = 0
    private var skipQues = 0
    private var totalQues = 1
    private lateinit var correctAn: String

    private var questionsQueue: Queue<Question> = LinkedList<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentAnsweringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        _ID = intent.getStringExtra("ID").toString()

        timer = Timer()

        binding.listViewOption.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        binding.listViewOption.setSelector(android.R.color.darker_gray);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        menu.findItem(R.id.action_pause).isVisible = true
        menu.findItem(R.id.action_settings).isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_pause -> {
                //TODO: Save state of current assessment
                saveState()

                runOnUiThread {
                    Thread.sleep(200)
                    Toast.makeText(applicationContext, "Current assessment progress had been stored"
                    , Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onStart() {
        super.onStart()
        loadPage(applicationContext, object: AssessmentCallBack{
            override fun onCallBack(assessment: Assessment) {
                binding.textViewTitle.text = assessment.title
                binding.textViewTotalQuestions.text = assessment.questions?.size.toString()
            }
        })

        overlayCountdown()
        showQues(position)
    }

    override fun onPause() {
        super.onPause()

        //TODO: store the current state of assessment
    }

    override fun onResume() {
        super.onResume()

        //TODO: restore the current state of assessment
    }

    private fun saveState(){
        var bundle: Bundle = Bundle()

    }

    private fun startTimer(){
        Log.d(TAG, "startTimer")
        timerTask = object: TimerTask(){
            override fun run() {
                runOnUiThread {
                    time++
                    binding.textViewTimer.text = formatTimer(time)
//                    Log.d(TAG, "Timer running at ${time}, time => " + formatTimer(time))
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private fun formatTimer(time: Double): CharSequence? {
        val rounded: Int = time.roundToInt()

        val seconds = ((rounded % 86400) % 3600) % 60
        val minutes = ((rounded % 86400) % 3600) / 60
        val hours = ((rounded % 86400) / 3600)

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun overlayCountdown(){
        object : CountDownTimer(4000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val second = (millisUntilFinished / 1000).toInt()

                Log.d(TAG, "OnTick $second")
                when(second){
                    3 -> {
                        binding.textViewCountdown.setTextColor(Color.rgb(255,0,0))
                    }
                    2 -> {
                        binding.textViewCountdown.setTextColor(Color.rgb(255, 165, 0))
                    }
                    1 -> {
                        binding.textViewCountdown.setTextColor(Color.rgb(60, 179, 113))
                    }
                    0 -> {
                        binding.textViewCountdown.setTextColor(Color.rgb(0, 0, 255))
                        binding.textViewCountdown.text = "START"
                        return
                    }
                }
                binding.textViewCountdown.text = second.toString()
            }

            override fun onFinish() {
                Log.d(TAG, "Countdown finished")
                runOnUiThread {
                    Thread.sleep(100)
                    binding.constraintOverlay.visibility = View.GONE
                }
                startTimer()
            }
        }.start()

    }

    private fun loadPage(context: Context, callback : AssessmentCallBack){
        val database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("assessment/${_ID}")

        Log.d(TAG, "ID => ${_ID}")

        databaseReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onCallBack(
                    it.result.getValue(Assessment::class.java)!!
                )
            } else {
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                Log.e(TAG, it.exception!!.stackTraceToString())
            }
        }
    }

    private fun showQues(position: Int) {
        database = FirebaseDatabase.getInstance().getReference("assessment")
        database.child(_ID).child("questions").limitToFirst(position + 1).get().addOnSuccessListener {
            if (it.exists()) {
                Log.d(TAG, "Loaded with ${it.children.count()} records")
                Log.d(TAG, "Result => ${it.value}")

                val question: HashMap<String, Any?> = (it.value as ArrayList<Any?>)[0] as HashMap<String, Any?>

                correctAn = question["answer"].toString()
                binding.textViewQuestionDet.text = question["question"].toString()
                binding.textViewCurrentQuestion.text = (position + 1).toString()
                binding.listViewOption.adapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, (question["options"] as List<String>));
                binding.listViewOption.setOnItemClickListener {p0, p1, p2, p3 ->
                    binding.listViewOption.setItemChecked(p2, true)
                    binding.listViewOption.isEnabled = false
                    Log.d(TAG, "Option $p2 had been selected, checking answer")
                    val optionSelected = binding.listViewOption.getItemAtPosition(p2).toString()
                    if(optionSelected == correctAn){
                        Log.d(TAG, "Correct answer")
                    }else{
                        Log.d(TAG, "Wrong answer, selected option = $optionSelected")
                    }
                }
            }else{
                Log.d(TAG, "Nothing loaded")
            }
        }
    }

}