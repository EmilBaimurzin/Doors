package com.lucmon.keyk.game.ui.rules

import android.os.Bundle
import android.view.View
import com.lucmon.keyk.game.ui.other.ViewBindingFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lucmon.keyk.databinding.FragmentGameRulesBinding
import com.lucmon.keyk.game.domain.adapters.rules.GameRulesAdapter

class FragmentGameRules : ViewBindingFragment<FragmentGameRulesBinding>(FragmentGameRulesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rulesVP.adapter =
            GameRulesAdapter(childFragmentManager, lifecycle, arrayListOf(Page1(), Page2()))
        TabLayoutMediator(binding.dotsTabLayout, binding.rulesVP) { _, _ -> }.attach()
    }
}