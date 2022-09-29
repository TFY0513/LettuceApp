package com.example.lettuceapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Survey
import java.util.*


class SurveyAdapter (surveyList: List<Survey>) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>(){

    private val surveyList: List<Survey>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_survey, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SurveyAdapter.ViewHolder, position: Int) {
        val model: Survey = surveyList[position]

//        holder.cardSurveyBase.background = generateRandomColor()
//        holder.cardSurveyQuestion.background = generateRandomColor()
        holder.cardSurveyQuestion.setCardBackgroundColor(setRandomColor())
//        holder.cardSurveyBase.setCardBackgroundColor(setRandomColor())
//        holder.cardSurveyQuestion.radius = 8f
        holder.questionNumber.text = (position+1).toString()
        holder.question.text = model.question
        holder.category.text = model.category
    }

    override fun getItemCount(): Int {
        return surveyList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardSurveyBase: CardView
        val cardSurveyQuestion: CardView
        val questionNumber: TextView
        val question: TextView
        val category: TextView
        init {
            cardSurveyBase = itemView.findViewById(R.id.cardViewBaseAssessment)
            cardSurveyQuestion = itemView.findViewById(R.id.cardViewQuestionSurvey)
            questionNumber = itemView.findViewById(R.id.textViewQuestionNumber)
            question = itemView.findViewById(R.id.textViewSurveyQuestion)
            category = itemView.findViewById(R.id.textViewCategory)
        }
    }

    init {
        this.surveyList = surveyList
    }

    private fun setRandomColor() : Int{
        val r = Random()
        val red: Int = r.nextInt(255 - 0 + 1) + 0
        val green: Int = r.nextInt(255 - 0 + 1) + 0
        val blue: Int = r.nextInt(255 - 0 + 1) + 0

        return Color.rgb(red, green, blue)
    }

    private fun generateRandomColor() : GradientDrawable{
        val draw = GradientDrawable()
        draw.shape = GradientDrawable.RECTANGLE
        draw.setColor(setRandomColor())

        return draw;
    }

}