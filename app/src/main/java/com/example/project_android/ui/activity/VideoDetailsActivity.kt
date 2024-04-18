package com.example.project_android.ui.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.project_android.R
import com.example.project_android.data.remote.YoutubeDataAPI
import com.example.project_android.data.remote.TheMovieDatabaseAPI
import com.example.project_android.data.services.AdsServices
import com.example.project_android.viewmodel.CastDetailsViewModel
import com.google.android.gms.ads.AdView
import com.example.project_android.viewmodel.VideoViewModel


class VideoDetailsActivity : AppCompatActivity() {

    private lateinit var videoViewModel: VideoViewModel
    private lateinit var titlePage: TextView
    private lateinit var videoDescription: TextView
    private lateinit var mAdView: AdView
    private val adsServices = AdsServices(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)

        // Nhận dữ liệu từ Intent
        val videoName = intent.getStringExtra("videoName")
        val videoID = intent.getStringExtra("videoKey")

        videoViewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        titlePage = findViewById(R.id.video_title)
        videoDescription = findViewById(R.id.video_description)
        titlePage.text = videoName
        mAdView = findViewById(R.id.adView)

        val frameVideo =
            "<html><head><style>body, html { margin: 0; padding: 0; } iframe { width: 100%; height: 100%; border: none; } </style></head><body><iframe src=\"https://www.youtube.com/embed/$videoID\" frameborder=\"0\" allowfullscreen></iframe></body></html>"

        val displayYoutubeVideo = findViewById<WebView>(R.id.mWebView)
        displayYoutubeVideo.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        if (videoID != null) {
            videoViewModel.getVideoData(videoID) { videoDetails ->
                if (videoDetails != null) {
                    videoDescription.text = videoDetails
                }
            }
        }
        else {
            videoDescription.text = "Description not Available"
        }

        val webSettings = displayYoutubeVideo.settings
        webSettings.javaScriptEnabled = true
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8")
//Gọi hàm load quảng cáo
        adsServices.loadBanner(mAdView,this)
    }
}