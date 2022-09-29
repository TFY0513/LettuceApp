package com.example.lettuceapp.ui.assessment.category

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lettuceapp.adapter.AssessmentAdapter
import com.example.lettuceapp.databinding.FragmentActiveAssessmentBinding
import com.example.lettuceapp.firebase.AssessmentCallBack
import com.example.lettuceapp.model.Assessment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import java.util.*


class ActiveAssessmentFragment : Fragment() {

    private var _binding: FragmentActiveAssessmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentActive = -1 //haven't updated
    private var previousLoaded = -2 //haven't updated

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveAssessmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        retrieveAssessment()
        binding.layoutAssessmentCategory.recycleViewAssessment.layoutManager = linearLayoutManager

        val pullToRefresh = binding.pullToRefresh
        pullToRefresh.setOnRefreshListener {
            retrieveAssessment()
            pullToRefresh.isRefreshing = false
            var result = if(currentActive !== previousLoaded){
                (previousLoaded - currentActive ).toString() + "assessment(s) loaded"
            }else{
                "No new assessment"
            }
            previousLoaded = currentActive
            Toast.makeText(activity?.applicationContext,
                result, Toast.LENGTH_SHORT).show()
        }
        previousLoaded = currentActive
    }

    private fun retrieveAssessment(){
        retrieveAssessment(activity?.applicationContext!!, object: AssessmentCallBack{
            override fun onCallBack(count: Int, assessmentList: List<Assessment>) {
                binding.layoutAssessmentCategory.recycleViewAssessment.adapter = AssessmentAdapter(assessmentList)
                currentActive = count
            }
        })
    }

    private fun retrieveAssessment(context: Context, callback : AssessmentCallBack){
        val database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("assessment")

        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DATE, 10)

        databaseReference.orderByChild("active_timestamp").startAt("0.0").endAt((c.timeInMillis / 1000).toString()).get().addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onCallBack(
                    it.result.children.count(),
                    it.result.children.mapNotNull { doc ->
                        doc.getValue(Assessment::class.java)

                    }
                )
            } else {
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}