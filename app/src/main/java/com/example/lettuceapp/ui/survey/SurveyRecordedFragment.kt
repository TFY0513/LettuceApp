package com.example.lettuceapp.ui.survey

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.MainActivity
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentSurveyRecordedBinding

class SurveyRecordedFragment : Fragment() {
    private var _binding: FragmentSurveyRecordedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        /**
         * https://stackoverflow.com/questions/17738966/how-to-disable-back-button-pressed-in-android-fragment-class
         * https://stackoverflow.com/questions/55074497/how-to-add-onbackpressedcallback-to-fragment
         */
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        findNavController().navigate(R.id.action_surveyRecordedFragment_to_articleFragment)
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyRecordedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.action_settings).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                findNavController().navigate(R.id.action_surveyRecordedFragment_to_surveyFragment2)
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.check.check();

        binding.button2.setOnClickListener {
            startActivity(Intent(activity?.applicationContext, MainActivity::class.java))
        }
    }
}