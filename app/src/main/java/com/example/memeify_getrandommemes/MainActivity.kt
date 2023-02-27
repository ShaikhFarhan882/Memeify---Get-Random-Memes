package com.example.memeify_getrandommemes

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.example.memeify_getrandommemes.databinding.ActivityMainBinding
import com.example.memeify_getrandommemes.repository.MemeRepository
import com.example.memeify_getrandommemes.viewmodels.MainViewModel
import com.example.memeify_getrandommemes.viewmodels.MainViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var memeUrl : String
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
            memeUrl = it.url.toString()
            loadImage(memeUrl)
            linkSetup(it.postLink)
        })

        binding.downloadMeme.setOnClickListener {
           checkPermissions()
        }

    }

    private fun loadImage(url: String) {
        binding.progressBar.visibility = View.VISIBLE
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
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


    private fun downloadImage(context : Context, url : String){
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Downloading Image")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Meme.jpg")
        downloadManager.enqueue(request)
        Toast.makeText(this@MainActivity,"Downloading Started", Toast.LENGTH_SHORT).show()
    }


    private fun checkPermissions(){
        Dexter.withContext(this)
            .withPermissions(
               Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if(report.areAllPermissionsGranted()){
                        downloadImage(this@MainActivity,memeUrl)
                    }
                    else {
                        Toast.makeText(this@MainActivity,"No Storage permission found failed to download",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

}