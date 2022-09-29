package com.example.lettuceapp.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentQuizBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private var position = 1
    private var question= "question_"
    private lateinit var correctAn: String
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
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
        var questionNum = question+position


        database = FirebaseDatabase.getInstance().getReference("quiz")
        database.child(questionNum).get().addOnSuccessListener {
            if (it.exists()) {
                correctAn =  it.child("answer").value.toString()
                binding.textViewAnswer1.text= "a) "+it.child("option1").value.toString()
                binding.textViewAnswer2.text= "b) "+it.child("option2").value.toString()
                binding.textViewAnswer3.text= "c) "+it.child("option3").value.toString()

                binding.textViewQuestion.text=it.child("question").value.toString()
                binding.textViewQuesNum.text = "Q"+position+"/Q10"
            binding.textView5.text=correctAn.toString()
            }
        }
    }

    fun checkAns(answerSelected: String, option: String) {
        if(answerSelected == correctAn.toString()){
            if(option == "option1"){
                binding.textViewAnswer1.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer1.append("  correct answer")
            }
            else if(option == "option2"){
                binding.textViewAnswer2.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer2.append("  correct answer")
            }
            else{
                binding.textViewAnswer3.setBackgroundColor(getResources().getColor(R.color.correct_ans))
                binding.textViewAnswer3.append("  correct answer")
            }
        }
        else{
            if(option == "option1"){
                binding.textViewAnswer1.setBackgroundColor(getResources().getColor(R.color.wrong_ans))
            }
            else if(option == "option2"){
                binding.textViewAnswer2.setBackgroundColor(getResources().getColor(R.color.wrong_ans))
            }
            else{
                binding.textViewAnswer3.setBackgroundColor(getResources().getColor(R.color.wrong_ans))
            }

        }


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewAnswer1.setOnClickListener() {
            checkAns(binding.textViewAnswer1.text.toString(), "option1")
            binding.buttonNext.setEnabled(false)
            binding.textView6.text= binding.textViewAnswer1.text.toString()

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


        }
    }
//    private fun loadQuiz() {
//
//
//        quizViewModel.loadQuizList(object : QuizCallBack {
//            override fun onCallBack(value: List<Quiz>) {
//
//                binding.textViewAnswer1.text= value.toString()
//            }
//        })
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}