package com.example.lettuceapp.ui.article

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.ActivityArticleWebViewBinding

class ArticleWebView : AppCompatActivity() {

    private lateinit var binding: ActivityArticleWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("articleUrl").toString()
        val title = intent.getStringExtra("articleTitle").toString()

        this.title = title

        val webView = binding.articleWebview
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        if (binding.articleWebview.canGoBack())
            binding.articleWebview.goBack()
        else
            super.onBackPressed()
    }

}