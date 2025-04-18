package com.ex.quizapplication.view.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.ex.quizapplication.model.TopicResponse
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ex.quizapplication.R
import com.ex.quizapplication.model.TopicResponseItem


class TopicAdapter(
    private val topicList : List<TopicResponseItem>,
    private val click : (Int) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewClassHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewClassHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewClassHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewClassHolder, position: Int) {
        val singleTopic = topicList[position]
        holder.bind(singleTopic)

        holder.cv.setOnClickListener {
            click(singleTopic.id)
        }
    }

    override fun getItemCount() = topicList.size

    inner class TopicViewClassHolder(item:View) : RecyclerView.ViewHolder(item){

        val topicName : TextView = item.findViewById(R.id.topicName)
        val cv : CardView =item.findViewById(R.id.cvTopic)

        fun bind(topic : TopicResponseItem){
            topicName.text = topic.name
        }

    }

}