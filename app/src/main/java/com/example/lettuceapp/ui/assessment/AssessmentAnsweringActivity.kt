package com.example.lettuceapp.ui.assessment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Suppress("UNCHECKED_CAST")
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
    private var correctAn: Int = 0
    private var wrongQues: Int = 0
    private var totalQuestion = 0
    private lateinit var intervalSwitchQuestion: CountDownTimer
    private lateinit var completionDateTime: String
    private lateinit var elapsedTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentAnsweringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        _ID = intent.getStringExtra("ID").toString()

        timer = Timer()

        binding.listViewOption.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        binding.listViewOption.setSelector(android.R.color.darker_gray);

        binding.buttonSkip.setOnClickListener{
            skipQues++
            nextQuestionOrDie()
        }

        binding.buttonNextAssessment.isEnabled = false
        binding.buttonNextAssessment.setOnClickListener{
            intervalSwitchQuestion.cancel().apply {
                binding.textViewTimeout.text = null
            }
            nextQuestionOrDie()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        menu.findItem(R.id.action_pause).isVisible = true
        menu.findItem(R.id.action_settings).isVisible = false
        menu.findItem(R.id.action_signout).isVisible = false
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
//                startActivity(Intent(applicationContext, AssessmentStartActivity::class.java).apply {
//                    this.putExtra("ID", _ID)
//                    this.putExtra("TITLE", _ID)
//                })
//                finish()
                onBackPressed()
                true
            }
            else -> true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(applicationContext, AssessmentStartActivity::class.java).apply {
            this.putExtra("ID", _ID)
        })
        finish()
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

        binding.constraintOverlay.visibility = View.GONE
//        overlayCountdown()
        startTimer()
        showQues(position)
    }

    override fun onPause() {
        super.onPause()

        //TODO: store the current state of assessment
        saveState()
    }

    override fun onResume() {
        super.onResume()

        //TODO: restore the current state of assessment
        restoreState()
    }

    private fun saveState(){
        var bundle: Bundle = Bundle()

    }

    private fun restoreState(){
        var bundle: Bundle = Bundle()

    }

    private fun startTimer(){
        Log.d(TAG, "startTimer")
        timerTask = object: TimerTask(){
            override fun run() {
                runOnUiThread {
                    time++
                    binding.textViewTimer.text = formatTimer(time)
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
                        binding.textViewCountdown.text = "GO"
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

        Log.d(TAG, "ID => $_ID")

        databaseReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result.getValue(Assessment::class.java)!! as Assessment
                callback.onCallBack(
                    result
                )
                totalQuestion = result.questions?.size!!
            } else {
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                Log.e(TAG, it.exception!!.stackTraceToString())
            }
        }
    }

    private fun reloadLayoutState(){
        intervalSwitchQuestion = object : CountDownTimer(6000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                val sec = (millisUntilFinished / 1000).toInt()
                val result = String.format(
                    "%d s to proceed next question",
                    sec)
//                binding.textViewTimeout.text = (millisUntilFinished / 1000).toString()
//                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                binding.buttonSkip.text = String.format("%d%s" , sec, ".".repeat(sec))
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                nextQuestionOrDie()
            }
        }.start()
    }

    /**
     * Display question based on given position
     */
    @SuppressLint("SetTextI18n")
    private fun showQues(position: Int) {
        if(totalQuestion != 0 && position == totalQuestion){
            Log.d(TAG, "Suspect back or up button had been performed.. skip retrieving data")
            return
        }
        database = FirebaseDatabase.getInstance().getReference("assessment")
        database.child(_ID).child("questions").get().addOnSuccessListener {
            if (it.exists()) {
                Log.d(TAG, "Loaded with ${it.children.count()} records")
                Log.d(TAG, "Result => ${it.value}")

                val question: HashMap<String, Any?> = (it.value as ArrayList<Any?>)[position] as HashMap<String, Any?>

                correctAn = question["answer"].toString().toInt()
                binding.textViewQuestionDet.text = question["question"].toString()
                binding.textViewCurrentQuestion.text = (position + 1).toString()
                binding.listViewOption.adapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, (question["options"] as List<String>));
                binding.listViewOption.setOnItemClickListener {p0, p1, p2, p3 ->
                    binding.listViewOption.setItemChecked(p2, true)
                    binding.listViewOption.isEnabled = false
                    binding.buttonSkip.isEnabled = false
                    binding.buttonNextAssessment.isEnabled = true
                    Log.d(TAG, "Option $p2 had been selected, checking answer")
                    val optionSelected = binding.listViewOption.getItemAtPosition(p2).toString()
                    if(p2 == correctAn){
                        Log.d(TAG, "Correct answer")
                        binding.listViewOption.setSelector(R.color.correct_ans)
                        correctQues++
                    }else{
                        wrongQues++
                        Log.d(TAG, "Wrong answer, selected option = $optionSelected")
                        binding.listViewOption.setSelector(R.color.wrong_ans);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.listViewOption.getChildAt(correctAn).setBackgroundColor(getColor(R.color.correct_ans))
                        }
                    }
                    if(position != totalQuestion - 1){
                        reloadLayoutState()
                    }
                    binding.buttonSkip.isEnabled = true
                }
            }else{
                Log.d(TAG, "Nothing loaded")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun nextQuestionOrDie(){
        binding.textViewTimeout.text = null
        binding.buttonSkip.text = getString(R.string.skip)
        position++
        if(position == (totalQuestion - 1)){
            binding.buttonNextAssessment.visibility = View.GONE
            binding.buttonSkip.text = "Finish"
        }
        when(position){
            totalQuestion -> {
                finishAssessment()
            }
            else -> {
                binding.buttonNextAssessment.isEnabled = false
                binding.listViewOption.isEnabled = true
                binding.buttonSkip.isEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.listViewOption.setBackgroundColor(getColor(R.color.white))
                }
                binding.listViewOption.setSelector(android.R.color.white)
                showQues(position)
            }
        }
    }

    private fun finishAssessment(){
        completionDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Date())
        elapsedTime = binding.textViewTimer.text.toString()

        storeData()

        startActivity(Intent(this, AssessmentCompletedResultActivity::class.java).apply {
            this.putExtra("ID",_ID)
            this.putExtra("SKIPPED",skipQues)
            this.putExtra("CORRECT",correctQues)
            this.putExtra("WRONG",wrongQues)
            this.putExtra("TOTAL",totalQuestion)
            this.putExtra("COMPLETION_DATE",completionDateTime)
            this.putExtra("TIME_ELAPSED",elapsedTime)
        })
        finish()
    }

    @SuppressLint("SimpleDateFormat")
    private fun storeData() {
//        timerTask.cancel()
        GlobalScope.launch {
            FirebaseDatabase.getInstance().getReference("assessment").child(_ID).get().addOnSuccessListener {
                if(it.exists()){
                    val value = it.getValue(Assessment::class.java)

                    val result = HashMap<String,HashMap<String,Any?>>()
                    val resultChild = HashMap<String,Any?>()

                    resultChild["title"] = value?.title!!
                    resultChild["correct_questions"] = correctQues
                    resultChild["wrong_questions"] = wrongQues
                    resultChild["skipped_questions"] = skipQues
                    resultChild["time_elapsed"] = elapsedTime
                    resultChild["completion_date_time"] = completionDateTime

                    result[_ID] = resultChild

                    FirebaseDatabase.getInstance().getReference("assessment/completed/users")
                        .child(FirebaseAuth.getInstance().currentUser?.uid!!).setValue(result)
                        .addOnCanceledListener {
                            Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { err ->
                            Toast.makeText(applicationContext, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                        }

                }
            }.addOnFailureListener {
                Log.e(TAG, "Something goes wrong, please fix it!!! Exception in brief is ${it.message}")
            }

        }

    }

}