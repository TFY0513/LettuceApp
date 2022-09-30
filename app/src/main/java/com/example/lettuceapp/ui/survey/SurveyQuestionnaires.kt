package com.example.lettuceapp.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.TabPagerAdapter
import com.example.lettuceapp.databinding.FragmentSurveyQuestionnairesBinding
import com.example.lettuceapp.model.SurveyResponse
import com.example.lettuceapp.ui.survey.category.SurveyCategoryOneFragment
import com.example.lettuceapp.ui.survey.category.SurveyCategorySecondFragment
import com.example.lettuceapp.ui.survey.category.SurveyCategoryThirdFragment
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.app
import kotlinx.android.synthetic.main.card_layout_assessment.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap

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
                //Step 1 get current user id
                //TODO: Change checking of user id
                val userId = "123"

                val response = HashMap<String, Any?>()
                val childUpper = HashMap<String, Any?>()
                var child = HashMap<String, Any?>()
                //Step 2 retrieve from fragment for user response
                try{
                    for(i in 0 until (binding.pager.adapter?.count!!)){
                        val title = binding.tbLayout.getTabAt(i)?.text.toString()
                        val recyclerView = binding.pager[i].findViewById<RecyclerView>(R.id.recycleViewSurveyCard1)

                        var score = 0f
                        val count = recyclerView.adapter?.itemCount!!
                        for(j in 0 until count){
                            val view = recyclerView.findViewById<CardView>(R.id.cardViewBaseAssessment)
                                .findViewById<View>(R.id.constraintBaseSurveyCard)
                            val radio =view.findViewById<RadioGroup>(R.id.radioGroup)
                            when (radio.checkedRadioButtonId){
                                R.id.radioButtonScore1 -> score += 1
                                R.id.radioButtonScore2 -> score += 2
                                R.id.radioButtonScore3 -> score += 3
                                R.id.radioButtonScore4 -> score += 4
                                R.id.radioButtonScore5 -> score += 5
                                else -> {
                                    val alertDialog = AlertDialog.Builder(requireActivity())
                                    alertDialog.apply {
                                        setTitle(R.string.survey_required_alert_title)
                                        setMessage(String.format(getString(R.string.survey_required_alert_message)
                                            , j))
                                        setPositiveButton(R.string.survey_required_alert_positive){ dialog, _ ->
                                            dialog.dismiss()
                                        }
                                    }.show()
                                    radio.isFocusableInTouchMode = true;
                                    radio.requestFocus()
                                    throw Exception("[radio group -> " + j +  "]" + radio.id.toString() + " is required.")
                                }
                            }

                        }
                        val mean = score / count.toFloat()

                        child = HashMap(SurveyResponse.toMap(SurveyResponse(title, score, mean)))
                        childUpper[title] = child
                    }
                }catch(e:Exception){
                    Log.w("Survey check item ", e.stackTraceToString())
                    return false
                }
                response[userId] = childUpper

                //Insert into database
                GlobalScope.launch {
                    val database = FirebaseDatabase.getInstance()
                    val databaseReference =  database.getReference("survey/response")

                    try{
                        databaseReference.setValue(response)
                    }catch(e:Exception){
                        Toast.makeText(activity?.applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                //Response to success
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