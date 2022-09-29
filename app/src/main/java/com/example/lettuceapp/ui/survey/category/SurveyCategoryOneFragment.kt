package com.example.lettuceapp.ui.survey.category

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.ArticleAdapter
import com.example.lettuceapp.adapter.SurveyAdapter
import com.example.lettuceapp.databinding.FragmentSurveyCategoryOneBinding
import com.example.lettuceapp.firebase.ArticleCallBack
import com.example.lettuceapp.firebase.SurveyCallback
import com.example.lettuceapp.model.Article
import com.example.lettuceapp.model.Survey
import com.example.lettuceapp.ui.survey.SurveyFragment

class SurveyCategoryOneFragment : Fragment() {

    private var _binding: FragmentSurveyCategoryOneBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyCategoryOneBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        loadSurvey()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutCategory.textViewSectionTitle.text = getString(R.string.survey_section_one)
        binding.layoutCategory.recycleViewSurveyCard1.preserveFocusAfterLayout = true
    }

    private fun loadSurvey(){
        val cm = activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        if(isConnected){
            SurveyFragment.retrieveSurveyQuestion(activity?.applicationContext!!, getString(R.string.survey_section_one_name), object: SurveyCallback {
                override fun onCallBack(title: String, value: List<Survey>) {
                    binding.layoutCategory.recycleViewSurveyCard1.adapter = SurveyAdapter(value)
                }
            })
        }

        binding.layoutCategory.recycleViewSurveyCard1.layoutManager = linearLayoutManager
    }
}