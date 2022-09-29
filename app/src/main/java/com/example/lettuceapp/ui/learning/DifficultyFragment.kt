package com.example.lettuceapp.ui.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentDifficultyBinding


class DifficultyFragment : Fragment() {
    private  var _binding: FragmentDifficultyBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDifficultyBinding.inflate(inflater, container, false)


        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.easyQuiz.setOnClickListener{quizTime("easy") }
    }


    fun quizTime(difficulty: String){
        findNavController().navigate(R.id.action_difficultyFragment_to_quizFragment, Bundle().apply {
            putString("course", requireArguments().getString("course").toString())
            putString("difficulty", difficulty)
        })
    }

}