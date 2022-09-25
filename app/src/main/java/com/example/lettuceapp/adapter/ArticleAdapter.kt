package com.example.lettuceapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Article

class ArticleAdapter(articleArrayList: ArrayList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val articleArrayList: ArrayList<Article>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        val model: Article = articleArrayList[position]
        holder.articlePreview.setImageBitmap(model.backgroundImage)
        holder.updatedDate.text = model.articleDate.toString()
        holder.articleTitle.text = model.articleTitle
        holder.author.text = model.author
        holder.summary.text = model.summary
    }

    override fun getItemCount(): Int {
        return articleArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articlePreview: ImageView
        val updatedDate: TextView
        val articleTitle: TextView
        val author: TextView
        val summary: TextView
        init {
            articlePreview = itemView.findViewById(R.id.imageViewArticle)
            updatedDate = itemView.findViewById(R.id.textViewDateUpdated)
            articleTitle = itemView.findViewById(R.id.textViewArticleTitle)
            author = itemView.findViewById(R.id.textViewAuthor)
            summary = itemView.findViewById(R.id.textViewSummary)
        }
    }

    init {
        this.articleArrayList = articleArrayList
    }
}