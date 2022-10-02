package com.example.lettuceapp.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.TabPagerAdapter
import com.example.lettuceapp.databinding.FragmentSurveyQuestionnairesBinding
import com.example.lettuceapp.model.SurveyResponse
import com.example.lettuceapp.ui.survey.category.SurveyCategoryOneFragment
import com.example.lettuceapp.ui.survey.category.SurveyCategorySecondFragment
import com.example.lettuceapp.ui.survey.category.SurveyCategoryThirdFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.collections.HashMap

class SurveyQuestionnaires : Fragment() {

    private val TAG = "SurveyQuestionnaires"
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
        menu.findItem(R.id.action_signout).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_submit -> {
                //Step 1 get current user id
                //TODO: Change checking of user id
                val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

                Log.d(TAG, "Current logged user => $userId")

                val response = HashMap<String, Any?>()
                val childUpper = HashMap<String, Any?>()
                //Step 2 retrieve from fragment for user response
                try{
                    for(i in 0 until (binding.pager.adapter?.count!!)){
                        val title = binding.tbLayout.getTabAt(i)?.text.toString()
                        val pager = binding.pager[i]
                        val listView = pager.findViewById<ListView>(R.id.listViewSurveyCard1)

                        var score = 0f
                        val count = listView.adapter?.count!!
                        for(j in 0 until count){
                            val view = listView.getChildAt(j)?.findViewById<ConstraintLayout>(R.id.constraintBase)?.findViewById<CardView>(R.id.cardViewBaseAssessmentCompleted)
                                ?.findViewById<ConstraintLayout>(R.id.constraintBaseSurveyCard)
                            val radio = view?.findViewById<RadioGroup>(R.id.radioGroup)
                            when (radio?.checkedRadioButtonId){
                                R.id.radioButtonScore1 -> score += 1
                                R.id.radioButtonScore2 -> score += 2
                                R.id.radioButtonScore3 -> score += 3
                                R.id.radioButtonScore4 -> score += 4
                                R.id.radioButtonScore5 -> score += 5
                                else -> {
                                    //TODO: Solve ViewPager to destroy previous view in the list (not on screen)
//                                    val alertDialog = AlertDialog.Builder(requireActivity())
//                                    alertDialog.apply {
//                                        setTitle(R.string.survey_required_alert_title)
//                                        setMessage(String.format(getString(R.string.survey_required_alert_message)
//                                            , (j+1), (i+1)))
//                                        setPositiveButton(R.string.survey_required_alert_positive){ dialog, _ ->
//                                            listView.setSelection(j)
//                                            binding.tbLayout.selectTab(binding.tbLayout.getTabAt(i))
//                                            dialog.dismiss()
//                                        }
//                                    }.show()
//                                    throw Exception("[radio group -> " + (j+1) +  "]" + radio?.id.toString() + " is required.")
                                }
                            }

                        }
                        val mean = score / count.toFloat()

                        Log.d(TAG, "mean => ${mean}")
                        Log.d(TAG, "count => ${count}")
                        Log.d(TAG, "score => ${score}")

                        childUpper[title] = HashMap(SurveyResponse.toMap(SurveyResponse(title, score, mean)))
                    }
                }catch(e:Exception){
                    Log.e(TAG, e.stackTraceToString())
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