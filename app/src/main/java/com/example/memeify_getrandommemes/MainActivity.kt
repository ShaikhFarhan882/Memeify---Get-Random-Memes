package com.example.memeify_getrandommemes

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.example.memeify_getrandommemes.databinding.ActivityMainBinding
import com.example.memeify_getrandommemes.repository.MemeRepository
import com.example.memeify_getrandommemes.viewmodels.MainViewModel
import com.example.memeify_getrandommemes.viewmodels.MainViewModelFactory
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(MemeRepository())).get(MainViewModel::class.java)

        binding.nextMeme.setOnClickListener {
            mainViewModel.getMeme()
        }

        mainViewModel.memeData.observe(this, Observer {
            binding.data = mainViewModel
            it.url?.let {
                loadImage(it)
            }
            linkSetup(it.postLink)
        })

    }

    private fun loadImage(url: String) {
        binding.progressBar.visibility = View.VISIBLE
        Glide.with(this)
            .load(url)
            .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                    return false
                }

            }).into(binding.imageView)

    }

    private fun linkSetup(postLink: String) {
        var link = Link("Source")
            .setHighlightAlpha(.4f)
            .setBold(true)
            .setOnClickListener {
                handleLinks(postLink)
            }
        binding.sourceMeme.applyLinks(link)
    }

    private fun handleLinks(link : String){
        val intent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse(link))
        startActivity(intent)
    }

}