package com.example.lettuceapp.ui.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentMaterialBinding
import com.example.lettuceapp.databinding.FragmentVideoBinding


class VideoFragment : Fragment() {
    private  var _binding: FragmentVideoBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        val root: View = binding.root

        showVideo(requireArguments().getString("course").toString())

        return root
    }
    fun showVideo(course: String){
        var video1 = ""
        var video2 = ""
        var video3 = ""
        var video4 = ""
        var video5 = ""

        if(requireArguments().getString("course").toString()== "js"){
            video1=getResources().getString(R.string.js_videoTitle1);
             video2 = getResources().getString(R.string.js_videoTitle2);
             video3 = getResources().getString(R.string.js_videoTitle3);
             video4 = getResources().getString(R.string.js_videoTitle4);
             video5 = getResources().getString(R.string.js_videoTitle5);
        }
        else if(requireArguments().getString("course").toString() == "chem"){
            video1=getResources().getString(R.string.chem_videoTitle1);
            video2 = getResources().getString(R.string.chem_videoTitle2);
            video3 = getResources().getString(R.string.chem_videoTitle3);
            video4 = getResources().getString(R.string.chem_videoTitle4);
            video5 = getResources().getString(R.string.chem_videoTitle5);
        }
        else{
            video1=getResources().getString(R.string.acct_videoTitle1);
            video2 = getResources().getString(R.string.acct_videoTitle2);
            video3 = getResources().getString(R.string.acct_videoTitle3);
            video4 = getResources().getString(R.string.acct_videoTitle4);
            video5 = getResources().getString(R.string.acct_videoTitle5);
        }

        binding.textViewVideoTitle1.text=video1
        binding.textViewVideoTitle2.text=video2
        binding.textViewVideoTitle3.text=video3
        binding.textViewVideoTitle4.text=video4
        binding.textViewVideoTitle5.text=video5
    }

}