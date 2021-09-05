package com.stopkaaaa.develoverslife.memesfragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stopkaaaa.develoverslife.MainActivity

class MemesPageAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(title: String, fragment: Fragment) {
        fragments.add(fragment)
        titles.add(title)
    }

    fun getTitles(): List<String> {
        return titles
    }
}
