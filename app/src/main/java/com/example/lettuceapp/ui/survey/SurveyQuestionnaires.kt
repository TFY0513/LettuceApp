package com.example.lettuceapp.ui.survey

import android.os.Bundle
import android.view.*
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.TabPagerAdapter
import com.example.lettuceapp.databinding.FragmentSurveyQuestionnairesBinding
import com.example.lettuceapp.ui.survey.category.SurveyCategoryOneFragment
import com.example.lettuceapp.ui.survey.category.SurveyCategorySecondFragment
import com.example.lettuceapp.ui.survey.category.SurveyCategoryThirdFragment

class SurveyQuestionnaires : Fragment() {

    private var _binding: FragmentSurveyQuestionnairesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tabPagerAdapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyQuestionnairesBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.action_settings).isVisible = false
        menu.findItem(R.id.action_submit).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_submit -> {
                //Perform submit

                //Step 1 get current user id

                //Insert into database

                //Toast to prompt successful update
//                Toast.makeText(activity?.applicationContext,
//                    getString(R.string.survey_submitted), Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_surveyQuestionnaires_to_surveyRecordedFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()

        if(binding.pager.isEmpty()){
            tabPagerAdapter = TabPagerAdapter(childFragmentManager)
            viewPager = binding.pager
            /**
             * How to stop refreshing viewpager content?
             * https://stackoverflow.com/questions/29610004/how-to-stop-refreshing-viewpager-content
             */
            viewPager.offscreenPageLimit = 3
            tabPagerAdapter.addFragment(SurveyCategoryOneFragment(), getString(R.string.survey_section_one_name))
            tabPagerAdapter.addFragment(SurveyCategorySecondFragment(), getString(R.string.survey_section_two_name))
            tabPagerAdapter.addFragment(SurveyCategoryThirdFragment(), getString(R.string.survey_section_three_name))
            viewPager.adapter = tabPagerAdapter
            binding.tbLayout.setupWithViewPager(viewPager)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}