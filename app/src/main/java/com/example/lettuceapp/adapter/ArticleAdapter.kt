package com.example.lettuceapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lettuceapp.R
import com.example.lettuceapp.model.Article
import com.example.lettuceapp.ui.article.ArticleWebView
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.net.URL
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(articleList: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>(){

    private val articleList: List<Article>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_article, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        val model: Article = articleList[position]
        val urlImage = URL(model.image_preview_url)
        val result: Deferred<Bitmap?> = GlobalScope.async {
            urlImage.toBitmap()
        }
        GlobalScope.launch(Dispatchers.Main) {
            // show bitmap on image view when available
            holder.articlePreview.setImageBitmap(result.await())
        }

        val stamp = Timestamp(model.timestamp!! * 1000)
        val date = Date(stamp.time)
        holder.updatedDate.text = SimpleDateFormat("dd-MMMM-yyyy").format(date).toString()
        holder.articleTitle.text = model.title
        holder.author.text = model.author
        holder.summary.text = model.summary
        holder.itemView.setOnClickListener { view ->
            try{
                val intent = Intent(view.context, ArticleWebView::class.java)
                intent.putExtra("articleUrl", model.article_url)
                intent.putExtra("articleTitle", model.title)
                startActivity(view.context, intent, null)
            }catch (e:Exception){
                Toast.makeText(view.context.applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
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

    class articleOnClickListner(val clickListener: (article: Article) -> Unit){
        fun onClick(article: Article) = clickListener(article)
    }

    fun URL.toBitmap(): Bitmap?{
        return try {
            BitmapFactory.decodeStream(openStream())
        }catch (e: IOException){
            null
        }
    }

    init {
        this.articleList = articleList
    }


}