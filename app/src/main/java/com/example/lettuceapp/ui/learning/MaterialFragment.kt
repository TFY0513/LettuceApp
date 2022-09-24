package com.example.lettuceapp.ui.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentLearningBinding
import com.example.lettuceapp.databinding.FragmentMaterialBinding


class MaterialFragment : Fragment() {
    private  var _binding: FragmentMaterialBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)


        val root: View = binding.root

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.quiz.setOnClickListener{
            findNavController().navigate(R.id.action_materialFragment_to_difficultyFragment)
        }

    }
}