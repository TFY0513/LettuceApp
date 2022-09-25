package com.example.lettuceapp.ui.learning

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
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

        showCourse(requireArguments().getString("course").toString())

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quiz.setOnClickListener{quiz() }

        binding.notes.setOnClickListener{notes()}

        binding.videoLink.setOnClickListener{video()}
    }

    fun showCourse(course: String){
        if(requireArguments().getString("course").toString()== "js"){
            binding.textViewCourse.text="JavaScript"
        }
        else if(requireArguments().getString("course").toString() == "chem"){
            binding.textViewCourse.text="Chemistry"
        }
        else{
            binding.textViewCourse.text="Accounting"
        }

    }
    fun quiz(){
        findNavController().navigate(R.id.action_materialFragment_to_difficultyFragment, Bundle().apply {
            putString("course", requireArguments().getString("course").toString())
        })
    }

    fun video(){
        findNavController().navigate(R.id.action_materialFragment_to_videoFragment, Bundle().apply {
            putString("course", requireArguments().getString("course").toString())
        })
    }

    fun notes(){
        var url=""
        if(requireArguments().getString("course").toString()== "js"){
            url = "https://www.w3schools.com/js/"
        }
        else if(requireArguments().getString("course").toString() == "chem"){
            url = "https://www.grandinetti.org/general-chemistry-tutorial"
        }
        else{
            url = "https://www.guru99.com/accounting.html"
        }

        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}