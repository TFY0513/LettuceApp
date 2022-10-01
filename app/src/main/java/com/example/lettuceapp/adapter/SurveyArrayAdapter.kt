package com.example.lettuceapp.adapter

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Survey
import java.util.*


class SurveyArrayAdapter (context: Context, layoutResource: Int, surveyList: List<Survey>) : ArrayAdapter<Survey>(context, layoutResource, surveyList) {

    private val surveyList: List<Survey>
    private val layoutResource: Int

    override fun getItem(position: Int): Survey = Survey()

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if(convertView == null){ //First loaded
            val survey = surveyList[position]
            view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_survey, parent, false)

            val cardSurveyQuestion: CardView = view.findViewById(R.id.cardViewQuestionSurvey)
            val questionNumber: TextView = view.findViewById(R.id.textViewQuestionNumber)
            val question: TextView = view.findViewById(R.id.textViewSurveyQuestion)
            val category: TextView = view.findViewById(R.id.textViewCategory)

            cardSurveyQuestion.setCardBackgroundColor(setRandomColor())
            questionNumber.text = (position+1).toString()
            question.text = survey.question
            category.text = survey.category
        }else{ //Subsequence get view instance
            view = convertView
        }

        return view
    }

    override fun getViewTypeCount(): Int {
        //Count=Size of ArrayList.
        return surveyList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun setRandomColor() : Int{
        val r = Random()
        val red: Int = r.nextInt(255 - 0 + 1) + 0
        val green: Int = r.nextInt(255 - 0 + 1) + 0
        val blue: Int = r.nextInt(255 - 0 + 1) + 0

        return Color.rgb(red, green, blue)
    }

    init {
        this.surveyList = surveyList
        this.layoutResource = layoutResource
    }
}