package com.example.lettuceapp.ui.learning

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentQuizBinding
import com.example.lettuceapp.model.QuizResult
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask


class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private var position = 1
    private var question = "question_"

    private var correctQues = 0
    private var skipQues = 0
    private var totalQues = 1
    private lateinit var correctAn: String

    private lateinit var quizID: String


    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference

    private val quizViewModel: QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuizBinding.inflate(inflater, container, false)

        val root: View = binding.root



        showQues(position)
        //loadQuiz()
        return root
    }

    fun showQues(position: Int) {
        var questionNum = question + position


        database = FirebaseDatabase.getInstance().getReference("quiz")
        database.child(questionNum).get().addOnSuccessListener {
            if (it.exists()) {
                correctAn = it.child("answer").value.toString()
                binding.textViewAnswer1.text = "a) " + it.child("option1").value.toString()
                binding.textViewAnswer2.text = "b) " + it.child("option2").value.toString()
                binding.textViewAnswer3.text = "c) " + it.child("option3").value.toString()

                binding.textViewQuestion.text = it.child("question").value.toString()
                binding.textViewQuesNum.text = "Q" + position + "/Q10"

            }
        }
    }

    fun checkAns(answerSelected: String, option: String) {

        binding.textViewAnswer1.setClickable(false)
        binding.textViewAnswer2.setClickable(false)
        binding.textViewAnswer3.setClickable(false)

        var answer = answerSelected.substring(3)

        if (answer == correctAn.toString()) {
            if (option == "option1") {
                binding.textViewAnswer1.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer1.append("   (correct answer)")
            } else if (option == "option2") {
                binding.textViewAnswer2.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer2.append("  (correct answer)")
            } else {
                binding.textViewAnswer3.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer3.append("  (correct answer)")
            }
            correctQues++

        } else {
            if (option == "option1") {
                binding.textViewAnswer1.setBackgroundColor(getResources().getColor(R.color.wrong_ans))
                binding.textViewAnswer1.append("  (wrong answer)")
            } else if (option == "option2") {
                binding.textViewAnswer2.setBackgroundColor(getResources().getColor(R.color.wrong_ans))
                binding.textViewAnswer2.append("  (wrong answer)")
            } else {
                binding.textViewAnswer3.setBackgroundColor(getResources().getColor(R.color.wrong_ans))
                binding.textViewAnswer3.append("  (wrong answer)")
            }

            if (correctAn == binding.textViewAnswer1.text.substring(3)) {
                binding.textViewAnswer1.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer1.append("  (correct answer)")
            } else if (correctAn == binding.textViewAnswer2.text.substring(3)) {
                binding.textViewAnswer2.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer2.append("  (correct answer)")
            } else {
                binding.textViewAnswer3.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer3.append("  (correct answer)")
            }

        }

        if (position == 10) {
            object : CountDownTimer(6000, 1000) {

                // Callback function, fired on regular interval
                override fun onTick(millisUntilFinished: Long) {
                    binding.textViewTiming.setText((millisUntilFinished / 1000).toString())
                }

                // Callback function, fired
                // when the time is up
                override fun onFinish() {
                    storeData()

                    findNavController().navigate(
                        R.id.action_quizFragment_to_quizResultFragment,
                        Bundle().apply {
                            putString("quizID", quizID)
                        })

                }
            }.start()

        } else {
            totalQues++
            position++

            object : CountDownTimer(6000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.textViewTiming.setText((millisUntilFinished / 1000).toString())
                }

                override fun onFinish() {
                    binding.textViewAnswer1.setClickable(true)
                    binding.textViewAnswer2.setClickable(true)
                    binding.textViewAnswer3.setClickable(true)

                    binding.buttonNext.setEnabled(true)
                    binding.textViewTiming.setText(null)
                    resetQues()
                    showQues(position)

                }
            }.start()
        }


    }

    fun storeData() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        database2 = FirebaseDatabase.getInstance().getReference("quiz_result")
        quizID = database2.push().key!!
        val quizResult = QuizResult(
            quizID, "UID00001",currentDate, correctQues, skipQues,

            requireArguments().getString("course").toString(),
            totalQues,
            requireArguments().getString("difficulty").toString()
        )

        database2.child(quizID).setValue(quizResult)
            .addOnCanceledListener {
                Toast.makeText(this?.context, "Done", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { err ->
                Toast.makeText(this?.context, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }


    }

    fun resetQues() {
        binding.textViewAnswer1.background = resources.getDrawable(R.drawable.quiz_box_answer)
        binding.textViewAnswer2.background = resources.getDrawable(R.drawable.quiz_box_answer)
        binding.textViewAnswer3.background = resources.getDrawable(R.drawable.quiz_box_answer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewAnswer1.setOnClickListener() {
            checkAns(binding.textViewAnswer1.text.toString(), "option1")
            binding.buttonNext.setEnabled(false)

        }

        binding.textViewAnswer2.setOnClickListener() {
            checkAns(binding.textViewAnswer2.text.toString(), "option2")
            binding.buttonNext.setEnabled(false)
        }

        binding.textViewAnswer3.setOnClickListener() {
            checkAns(binding.textViewAnswer3.text.toString(), "option3")
            binding.buttonNext.setEnabled(false)
        }
        binding.buttonNext.setOnClickListener {
            skipQues++
            totalQues++
            position++

            if (position == 10) {
                binding.buttonNext.text = "Finish"
            }
            if (position <= 10) {
                showQues(position)
            }
            if (position == 11) {
                totalQues--
                storeData()

                findNavController().navigate(
                    R.id.action_quizFragment_to_quizResultFragment,
                    Bundle().apply {
                        putString("quizID", quizID)
                    })
            }
            binding.textView6.text = position.toString()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}