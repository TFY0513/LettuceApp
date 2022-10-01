package com.example.lettuceapp.ui.assessment.category

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.AssessmentAdapter
import com.example.lettuceapp.databinding.FragmentUpcomingAssessmentBinding
import com.example.lettuceapp.firebase.AssessmentCallBack
import com.example.lettuceapp.model.Assessment
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class UpcomingAssessmentFragment : Fragment() {
    private var _binding: FragmentUpcomingAssessmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentActive = 0 //haven't updated
    private var previousLoaded = 0 //haven't updated

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingAssessmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cm = activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected){
            binding.layoutAssessmentCategory.constraintWarning.visibility = View.VISIBLE
            binding.layoutAssessmentCategory.imageViewIcon.setImageResource(R.drawable.ic_baseline_wifi_off_24)
            binding.layoutAssessmentCategory.textViewWarning.text = getString(R.string.connection_error)
            binding.layoutAssessmentCategory.textViewWarning.setOnClickListener{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    // only for android 10 and above
                    val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                    startActivityForResult(panelIntent, 0)
                }else{
                    startActivityForResult( Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                }
            }
        }else{
            binding.layoutAssessmentCategory.constraintWarning.visibility = View.INVISIBLE
        }

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        retrieveAssessment()
        binding.layoutAssessmentCategory.recycleViewAssessment.layoutManager = linearLayoutManager

        val pullToRefresh = binding.pullToRefresh
        pullToRefresh.setOnRefreshListener {
            if(isConnected){
                retrieveAssessment()
                var result = if(currentActive !== previousLoaded){
                    (currentActive - previousLoaded).toString() + " upcoming assessment(s) loaded"
                }else{
                    "No new upcoming assessment"
                }
                previousLoaded = currentActive
                Toast.makeText(activity?.applicationContext,
                    result, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity?.applicationContext,
                    getString(R.string.check_connection), Toast.LENGTH_SHORT).show()
            }
            pullToRefresh.isRefreshing = false
        }
        previousLoaded = currentActive
    }

    private fun retrieveAssessment(){
        retrieveAssessment(activity?.applicationContext!!, object: AssessmentCallBack{
            override fun onCallBack(count: Int, assessmentList: List<Assessment>) {
                if(assessmentList.isEmpty()){
                    binding.layoutAssessmentCategory.imageViewIcon.setImageResource(R.drawable.ic_baseline_calendar_month_24)
                    binding.layoutAssessmentCategory.textViewWarning.text = getString(R.string.assessment_empty_upcoming)
                    binding.layoutAssessmentCategory.constraintWarning.visibility = View.VISIBLE
                    return
                }
                binding.layoutAssessmentCategory.constraintWarning.visibility = View.INVISIBLE
                binding.layoutAssessmentCategory.recycleViewAssessment.adapter = AssessmentAdapter(AssessmentAdapter.Type.UPCOMING, assessmentList)
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

        databaseReference.orderByChild("active_timestamp").startAt((c.timeInMillis / 1000).toString()).get().addOnCompleteListener {
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