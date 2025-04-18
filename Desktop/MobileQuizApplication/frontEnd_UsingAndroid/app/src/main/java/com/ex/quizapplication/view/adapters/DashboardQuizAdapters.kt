package com.ex.quizapplication.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ex.quizapplication.model.Quizze
import com.ex.quizapplication.R

class DashboardQuizAdapters(
    private val quizList: List<Quizze>,
    private val onClick:(Quizze) ->Unit
) : RecyclerView.Adapter<DashboardQuizAdapters.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_livequiz, parent, false)
        return QuizViewHolder(view)
    }


    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quizObj = quizList[position]
        holder.bind(quizObj)
        Log.d("QUIZDATA", quizObj.totalQues.toString())

        holder.quizLayout.setOnClickListener {
            onClick(quizObj)
        }
    }

    override fun getItemCount() = quizList.size

    inner class QuizViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val quizName: TextView = item.findViewById(R.id.tvQuizTitle)
        val quizTotalQues: TextView = item.findViewById(R.id.tvQuizQuestions)
        val time: TextView = item.findViewById(R.id.tvQuizTime)
        val quizLayout : CardView = item.findViewById(R.id.cvQuiz)

        fun bind(quiz:Quizze){
            quizName.text = quiz.title
            quizTotalQues.text = quiz.totalQues.toString()
            time.text = quiz.time
        }


    }


}