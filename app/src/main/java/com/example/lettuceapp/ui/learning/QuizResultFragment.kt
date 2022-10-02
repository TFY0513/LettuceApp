package com.example.lettuceapp.ui.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentQuizBinding
import com.example.lettuceapp.databinding.FragmentQuizResultBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QuizResultFragment : Fragment() {
    private var _binding: FragmentQuizResultBinding? = null

    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuizResultBinding.inflate(inflater, container, false)

        val root: View = binding.root

        Toast.makeText(this?.context, R.string.complete_msg, Toast.LENGTH_SHORT).show()
        showResult(requireArguments().getString("quizID").toString())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRedo.setOnClickListener {
            retake()
        }
    }

    fun retake() {
        findNavController().navigate(R.id.action_quizResultFragment_to_learningFragment)

    }

    fun showResult(quizID: String) {
        database = FirebaseDatabase.getInstance().getReference("quiz_result")
        database.child(quizID).get().addOnSuccessListener {
            if (it.exists()) {
                val totalQues = it.child("totalQues").value.toString().toInt()
                val correctQues = it.child("correctQues").value.toString().toInt()
                val mark = (correctQues.toDouble() / totalQues.toDouble()) * 100
                var result = ""
                if (mark >= 50.00) {
                    result = "Pass"
                    binding.textViewQuizResultGet.setTextColor(getResources().getColor(R.color.correct_ans))
                } else {
                    result = "Failed"
                    binding.textViewQuizResultGet.setTextColor(getResources().getColor(R.color.wrong_ans))
                }
//                binding.textViewUsernameGet.text = it.child("userID").value.toString()
                binding.textViewQuizCourseGet.text = it.child("course").value.toString()
                binding.textViewDateGet.text = it.child("date").value.toString()
                binding.textViewQuizLvlGet.text = it.child("quiz_level").value.toString()
                binding.textViewCorrectQuesGet.text = it.child("correctQues").value.toString()
                binding.textViewWrongQuesGet.text = (it.child("totalQues").value.toString()
                    .toInt() - it.child("correctQues").value.toString().toInt()).toString()
                binding.textViewTotalQuesGet.text = it.child("totalQues").value.toString()
                binding.textViewSkipQuesGet.text = it.child("skipQues").value.toString()
                binding.textViewQuizResultGet.text = result.toString()
                binding.textViewMarkGet.text = mark.toString() + "%"


            }
        }
    }

}