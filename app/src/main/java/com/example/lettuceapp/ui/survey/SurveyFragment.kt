package com.example.lettuceapp.ui.survey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentSurveyBinding
import com.example.lettuceapp.firebase.SurveyCallback
import com.example.lettuceapp.model.Survey
import com.google.firebase.database.FirebaseDatabase

class SurveyFragment : Fragment() {

    private var _binding: FragmentSurveyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var hasRecord = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.action_settings).isVisible = false
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hasRecord = checkRecord()

        binding.buttonBegin.setOnClickListener{
            if(hasRecord){
                val alertDeleteDialog = AlertDialog.Builder(requireActivity())
                alertDeleteDialog.apply {
                    setTitle(R.string.survey_submitted_header)
                    setMessage(R.string.survey_recorded_redundancy)
                    setPositiveButton(android.R.string.ok){ _,_ ->
                        findNavController().navigateUp()
                    }
                }.show()
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_surveyFragment2_to_surveyQuestionnaires)
        }

        binding.textViewLink.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://link.springer.com/article/10.1007/s10984-008-9050-7")))
        }

        binding.textViewEmail.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + getString(R.string.email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.survey_support_subject)) //seem like not working =.=
            startActivity(intent)
        }

    }

    private fun checkRecord() : Boolean{
        val authUserID = "123"
        var result = false;

        checkResponded(authUserID, object: SurveyCallback{
            override fun onCallBack(title: String, value: List<Survey>) {/*Not used*/}

            override fun onCallBack(responded: Boolean) {
                result = responded
            }
        })
        return result;
    }

    private fun checkResponded(user: String, callback : SurveyCallback){
        val database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("survey/response")

        //get based on user id
        databaseReference.child(user).get().addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onCallBack(it.result.hasChildren())
            } else {
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object{
        fun retrieveSurveyQuestion(context: Context, category: String, callback : SurveyCallback){
            val database = FirebaseDatabase.getInstance()
            val databaseReference =  database.getReference("survey")

            databaseReference.child("questionnaire").child(category).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val surveys = it.result.children.mapNotNull { doc ->
                        doc.getValue(Survey::class.java)
                    }
//                    Toast.makeText(context, "Result on " + category + " is " + it.result.children.count().toString(), Toast.LENGTH_SHORT).show()
                    callback.onCallBack(it.result.key!!, surveys)
                } else {
                    Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}