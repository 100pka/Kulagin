package com.stopkaaaa.develoverslife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.stopkaaaa.develoverslife.databinding.ActivityMainBinding
import com.stopkaaaa.develoverslife.memesfragment.MemesFragment
import com.stopkaaaa.develoverslife.memesfragment.MemesPageAdapter
import com.stopkaaaa.develoverslife.memesfragment.TabTitles

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pageAdapter: MemesPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createMemesFragments()
    }

    private fun createMemesFragments() {
        pageAdapter = MemesPageAdapter(this)
        enumValues<TabTitles>().forEach {
            pageAdapter.addFragment(
                it.title,
                MemesFragment.newInstance(it)
            )
        }
        binding.viewPager.adapter = pageAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pageAdapter.getTitles()[position]
        }.attach()
    }
}