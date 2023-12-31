package com.lucmon.keyk.game.domain.adapters.rules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class GameRulesAdapter(fm: FragmentManager, lifecycle: Lifecycle, list: ArrayList<Fragment>) :
    FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = list

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]
}



