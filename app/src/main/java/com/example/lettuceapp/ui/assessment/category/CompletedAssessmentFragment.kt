package com.example.lettuceapp.ui.assessment.category

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.AssessmentAdapter
import com.example.lettuceapp.databinding.FragmentCompletedAssessmentBinding
import com.example.lettuceapp.firebase.AssessmentCallBack
import com.example.lettuceapp.model.Assessment
import com.example.lettuceapp.ui.assessment.AssessmentTabFragment
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CompletedAssessmentFragment : Fragment() {

    private var _binding: FragmentCompletedAssessmentBinding? = null

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
        _binding = FragmentCompletedAssessmentBinding.inflate(inflater, container, false)

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
                    (currentActive - previousLoaded).toString() + " completed assessment(s) loaded"
                }else{
                    "No new completed assessment"
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

        binding.layoutAssessmentCategory.textViewWarning.setOnClickListener{
            var parent: AssessmentTabFragment =  this.requireParentFragment() as AssessmentTabFragment
            parent.changeTab(0)
        }
    }

    private fun retrieveAssessment(){
        retrieveAssessment(activity?.applicationContext!!, object: AssessmentCallBack {
            override fun onCallBack(count: Int, assessmentList: List<Assessment>) {
                if(assessmentList.isEmpty()){
                    binding.layoutAssessmentCategory.imageViewIcon.setImageResource(R.drawable.ic_baseline_library_add_check_24)
                    binding.layoutAssessmentCategory.textViewWarning.text = getString(R.string.assessment_empty_completed)
                    binding.layoutAssessmentCategory.constraintWarning.visibility = View.VISIBLE
                    return
                }
                binding.layoutAssessmentCategory.constraintWarning.visibility = View.INVISIBLE
                binding.layoutAssessmentCategory.recycleViewAssessment.adapter = AssessmentAdapter(
                    AssessmentAdapter.Type.UPCOMING, assessmentList)
                currentActive = count
            }
        })
    }

    //TODO: Retrieve completed assessment result
    private fun retrieveAssessment(context: Context, callback : AssessmentCallBack){
        val database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("assessment/completed/users")

        databaseReference.get().addOnCompleteListener {
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