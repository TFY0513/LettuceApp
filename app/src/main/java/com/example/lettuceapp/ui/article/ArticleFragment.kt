package com.example.lettuceapp.ui.article

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lettuceapp.adapter.ArticleAdapter
import com.example.lettuceapp.databinding.FragmentArticleBinding
import com.example.lettuceapp.firebase.ArticleCallBack
import com.example.lettuceapp.model.Article
import com.google.firebase.database.*


class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = this.javaClass.name

    private lateinit var database: FirebaseDatabase
    private val articleViewModel: ArticleViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCheckConnection.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // only for android 10 and above
                val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                startActivityForResult(panelIntent, 0)
            }else{
                startActivityForResult( Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadArticle()

        val pullToRefresh = binding.pullToRefresh
        pullToRefresh.setOnRefreshListener {
            loadArticle()
            pullToRefresh.isRefreshing = false
        }
    }

    private fun loadArticle(){
        val cm = activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        if(isConnected){
            articleViewModel.loadArticleList(object: ArticleCallBack {
                override fun onCallBack(value: List<Article>) {
                    binding.recycleViewArticle.adapter = ArticleAdapter(value)
                }
            })
            binding.textViewWarnNoInternet.visibility = View.INVISIBLE
            binding.imageViewWifiDiabled.visibility = View.INVISIBLE
            binding.buttonCheckConnection.visibility = View.INVISIBLE
            binding.recycleViewArticle.visibility = View.VISIBLE
        }else{
            Log.w(TAG, "Device not connected to network")
            binding.textViewWarnNoInternet.visibility = View.VISIBLE
            binding.imageViewWifiDiabled.visibility = View.VISIBLE
            binding.recycleViewArticle.visibility = View.INVISIBLE
            binding.buttonCheckConnection.visibility = View.VISIBLE

        }

        binding.recycleViewArticle.layoutManager = linearLayoutManager

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}