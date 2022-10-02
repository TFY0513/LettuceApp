package com.example.lettuceapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Assessment
import com.example.lettuceapp.ui.assessment.AssessmentCompletedResultActivity
import com.example.lettuceapp.ui.assessment.AssessmentStartActivity
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AssessmentAdapter (assessmentType: AssessmentAdapter.Type, assessmentList: List<Assessment>, questionId: List<String>? = ArrayList()) :
    RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    private val assessmentList: List<Assessment>
    private val assessmentType: AssessmentAdapter.Type
    //Only for completed
    private val questionId: List<String>?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout_assessment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssessmentAdapter.ViewHolder, position: Int) {
        val model: Assessment = assessmentList[position]

        holder.date.text = model.date
        holder.title.text = model.title
        holder.desc.text = model.description
        holder.duration.text = model.duration
        holder.questionCount.text = model.questions?.count().toString()
        holder.itemView.setOnClickListener { view ->
            try {
                when (assessmentType){
                    Type.ACTIVE -> {
                        val intent = Intent(view.context, AssessmentStartActivity::class.java)
                        intent.putExtra("TITLE", model.title)
//                        intent.putExtra("DATE", model.date)
                        intent.putExtra("ID", questionId!![position])
                        ContextCompat.startActivity(view.context, intent, null)
                    }
                    Type.COMPLETED -> {
                        //Redirect to show result
                        holder.takeTest.text = "Check\nresult"
                        val intent = Intent(view.context, AssessmentCompletedResultActivity::class.java)
                        intent.putExtra("ID",  questionId!![position])
                        ContextCompat.startActivity(view.context, intent, null)
                    }
                    Type.UPCOMING -> {
                        val days = differenceBetweenTimestamps(Calendar.getInstance().timeInMillis, (model.active_timestamp?.toLong()!! * 1000))
                        val alertDialog = AlertDialog.Builder(view.context)
                        alertDialog.apply {
                            setTitle(R.string.assessment_upcoming_alert)
                            setMessage(String.format(view.context.resources.getString(R.string.assessment_upcoming_alert_desc)
                                , model.title, days))
                            setPositiveButton(android.R.string.ok){ dialog,_ ->
                                dialog.dismiss()
                            }
                        }.show()
                    }
                }

            } catch (e: Exception) {
                Log.e("",e.stackTraceToString())
            }
        }
    }

    override fun getItemCount(): Int {
        return assessmentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView
        val title: TextView
        val desc: TextView
        val duration: TextView
        val questionCount: TextView
        val takeTest: TextView

        init {
            date = itemView.findViewById(R.id.textViewDate)
            title = itemView.findViewById(R.id.textViewTitle)
            desc = itemView.findViewById(R.id.textViewDescription)
            duration = itemView.findViewById(R.id.textViewDuration)
            questionCount = itemView.findViewById(R.id.textViewQuestionCount)
            takeTest = itemView.findViewById(R.id.textViewTakeTest)
        }
    }

    public enum class Type{
        ACTIVE,UPCOMING,COMPLETED
    }

    private fun differenceBetweenTimestamps(start: Long, end: Long ): Long {
        return TimeUnit.MILLISECONDS.toDays(end - start)
    }

    init {
        this.assessmentList = assessmentList
        this.assessmentType = assessmentType
        this.questionId = questionId
    }

}