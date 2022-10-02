package com.example.lettuceapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Assessment
import com.example.lettuceapp.ui.assessment.AssessmentCompletedResultActivity
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AssessmentCompletedAdapter (assessmentType: AssessmentAdapter.Type, assessmentList: List<Assessment>, questionId: List<String>? = ArrayList()) :
    RecyclerView.Adapter<AssessmentCompletedAdapter.ViewHolder>() {

    private val assessmentList: List<Assessment>
    private val assessmentType: AssessmentAdapter.Type
    //Only for completed
    private val questionId: List<String>?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentCompletedAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout_assessment_completed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssessmentCompletedAdapter.ViewHolder, position: Int) {
        val model: Assessment = assessmentList[position]

        holder.date.text = model.date
        holder.title.text = model.title
        holder.desc.text = model.description
        holder.duration.text = model.duration
        holder.questionCount.text = model.questions?.count().toString()
        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, AssessmentCompletedResultActivity::class.java)
            intent.putExtra("ID",  questionId!![position])
            ContextCompat.startActivity(view.context, intent, null)
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

        init {
            date = itemView.findViewById(R.id.textViewDate)
            title = itemView.findViewById(R.id.textViewTitle)
            desc = itemView.findViewById(R.id.textViewDescription)
            duration = itemView.findViewById(R.id.textViewDuration)
            questionCount = itemView.findViewById(R.id.textViewScore)
        }
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