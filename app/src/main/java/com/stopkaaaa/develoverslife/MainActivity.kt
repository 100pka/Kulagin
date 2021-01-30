package com.stopkaaaa.develoverslife

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stopkaaaa.develoverslife.data.LoadingState
import com.stopkaaaa.develoverslife.data.Mem
import com.stopkaaaa.develoverslife.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.currentMem.observe(this, this::bindMem)
        viewModel.loadingState.observe(this, this::bindState)
        bindListeners()
    }

    private fun bindListeners() {
        binding.backButton.setOnClickListener {
            viewModel.previousMem()
        }
        binding.forwardButton.setOnClickListener {
            viewModel.nextMem()
        }
    }

    private fun bindMem(mem: Mem) {
        mem.gifUrl?.let {
            Glide.with(this)
                .load(StringBuilder(it).insert(4, 's').toString())
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.isVisible = false
                        return false
                    }
                })
                .error(R.drawable.ic_no_image_placeholder)
                .into(binding.gifImageView)
        }
        binding.descriptionTextView.text = mem.description
    }

    private fun bindState(state: LoadingState) {
        when (state) {
            LoadingState.FIRST_MEM -> {
                binding.backButton.setImageResource(R.drawable.ic_back_button_not_available)
            }
            LoadingState.NEXT_MEM -> {
                binding.backButton.setImageResource(R.drawable.ic_back_button)
            }
            LoadingState.LOADING -> {
                binding.progressBar.isVisible = true
            }
            LoadingState.ERROR -> {
                binding.progressBar.isVisible = false
                Toast.makeText(this, "Something goes wrong...", Toast.LENGTH_LONG).show()
            }
        }
    }

}