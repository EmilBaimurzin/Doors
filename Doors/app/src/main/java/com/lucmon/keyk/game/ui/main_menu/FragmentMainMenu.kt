package com.lucmon.keyk.game.ui.main_menu

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.FragmentMainMenuBinding
import com.lucmon.keyk.game.ui.other.ViewBindingFragment
import com.lucmon.keyk.library.library.soundClickListener

class FragmentMainMenu : ViewBindingFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.fragmentMenu, inclusive = false)
            }
        })
        binding.apply {
            playButton.soundClickListener {
                findNavController().navigate(FragmentMainMenuDirections.actionFragmentMenuToFragmentBid())
            }

            settingsButton.soundClickListener {
                findNavController().navigate(R.id.action_fragmentMenu_to_fragmentSettings)
            }

            infoButton.soundClickListener {
                findNavController().navigate(R.id.action_fragmentMenu_to_fragmentRules)
            }
        }
    }
}