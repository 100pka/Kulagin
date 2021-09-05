package com.stopkaaaa.develoverslife.memesfragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stopkaaaa.develoverslife.R
import com.stopkaaaa.develoverslife.data.LoadingState
import com.stopkaaaa.develoverslife.data.Mem
import com.stopkaaaa.develoverslife.databinding.FragmentMainBinding

class MemesFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MemesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getSerializable(TITLE_KEY) as TabTitles
        viewModel =
            ViewModelProvider(this, MemesViewModelFactory(title)).get(MemesViewModel::class.java)
        viewModel.currentMem.observe(viewLifecycleOwner, this::bindMem)
        viewModel.loadingState.observe(viewLifecycleOwner, this::bindState)
        bindListeners()
    }

    private fun bindListeners() {
        binding.backButton.setOnClickListener { viewModel.previousMem() }
        binding.forwardButton.setOnClickListener { viewModel.nextMem() }
        binding.retryButton.setOnClickListener { viewModel.retryLoad() }
    }

    private fun bindMem(mem: Mem) {
        Glide.with(this)
            .load(httpToHttpsURL(mem.gifUrl))
            .listener(
                object : RequestListener<Drawable> {

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
                }
            )
            .error(R.drawable.ic_no_image_placeholder)
            .into(binding.gifImageView)
        binding.descriptionTextView.text = mem.description
    }

    private fun bindState(state: LoadingState) {
        when (state) {
            is LoadingState.Success -> {
                binding.progressBar.isVisible = false
                binding.errorStateView.isVisible = false
                binding.cardView.isVisible = true
                binding.backButton.isEnabled = !state.isFirstMem
            }
            is LoadingState.Error -> {
                binding.progressBar.isVisible = false
                showError(state.description)
            }
            is LoadingState.Loading -> {
                binding.progressBar.isVisible = true
            }
        }
    }

    private fun showError(description: String) {
        binding.errorStateView.isVisible = true
        binding.cardView.isVisible = false
        binding.errorDescriptionTextview.text = description
    }

    private fun httpToHttpsURL(httpURL: String): String {
        return StringBuilder(httpURL).insert(4, 's').toString()
    }

    companion object {
        private const val TAG = "MemesFragment"
        private const val TITLE_KEY = "title"

        @JvmStatic
        fun newInstance(title: TabTitles): MemesFragment {
            val args = Bundle().apply { putSerializable(TITLE_KEY, title) }
            return MemesFragment().apply { arguments = args }
        }
    }
}
