package com.example.lettuceapp.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lettuceapp.adapter.ArticleAdapter
import com.example.lettuceapp.databinding.FragmentArticleBinding
import com.example.lettuceapp.model.Article
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = this.javaClass.name

    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun loadArticle(){
        val cm = activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(isConnected){
            var adapterRVArticle:ArticleAdapter
            GlobalScope.launch {
                adapterRVArticle = ArticleAdapter(loadArticleFromDatabase())

                val linearLayoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)

                binding.recycleViewArticle.layoutManager = linearLayoutManager
                binding.recycleViewArticle.adapter = adapterRVArticle
            }
        }else{
            Log.w(TAG, "Device not connected to network")
        }

    }

    private fun loadArticleFromDatabase() : ArrayList<Article> {
        var arrayArticle: ArrayList<Article> = ArrayList()
        database = FirebaseDatabase.getInstance()
        val databaseReference =  database.getReference("Article")

        val get = databaseReference.child("*").orderByChild("timestamp").get()
        if(get.isSuccessful){
            var result = get.result
            Log.d(TAG, result.childrenCount.toString() )

        }else{
            Log.e(TAG, "Error on retrieving data")
        }

        return arrayArticle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}