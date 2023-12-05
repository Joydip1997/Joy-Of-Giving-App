package com.csr.beneficiary.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.csr.beneficiary.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity(){

    companion object{
        fun goToWebViewActivity(appContext : Context, webUrl : String) {
            val intent = Intent(appContext, WebViewActivity::class.java)
            intent.putExtra("WEB_URL",webUrl)
            appContext.startActivity(intent)
        }
    }

    private var _binding: ActivityWebViewBinding? = null
    private val binding: ActivityWebViewBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val webUrl = intent?.extras?.getString("WEB_URL").toString()
        binding.webview.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(webUrl)
        }
    }
}