package com.example.lettuceapp.ui.assessment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lettuceapp.R

class AssessmentFragment : Fragment() {

    companion object {
        fun newInstance() = AssessmentFragment()
    }

    private lateinit var viewModel: AssessmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_assessment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AssessmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}