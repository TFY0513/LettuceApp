package com.example.lettuceapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Assessment
import com.example.lettuceapp.ui.assessment.AssessmentAnsweringActivity
import java.lang.Exception

class AssessmentAdapter (assessmentList: List<Assessment>) :
    RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    private val assessmentList: List<Assessment>

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
                val intent = Intent(view.context, AssessmentAnsweringActivity::class.java)
                ContextCompat.startActivity(view.context, intent, null)
            } catch (e: Exception) {
                Toast.makeText(view.context.applicationContext, e.message, Toast.LENGTH_SHORT).show()
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

        init {
            date = itemView.findViewById(R.id.textViewDate)
            title = itemView.findViewById(R.id.textViewTitle)
            desc = itemView.findViewById(R.id.textViewDescription)
            duration = itemView.findViewById(R.id.textViewDuration)
            questionCount = itemView.findViewById(R.id.textViewQuestionCount)
        }
    }

    init {
        this.assessmentList = assessmentList
    }

}