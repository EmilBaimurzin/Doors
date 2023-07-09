package com.lucmon.keyk.game.ui.bonus

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.DialogBonusGameTextBinding
import com.lucmon.keyk.game.ui.other.ViewBindingFragment
import com.lucmon.keyk.library.library.soundClickListener

class DialogBonusGameText :
    ViewBindingFragment<DialogBonusGameTextBinding>(DialogBonusGameTextBinding::inflate) {
    private val viewModel: DialogViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.declineButton.soundClickListener {
            findNavController().popBackStack(R.id.fragmentBid, false)
        }

        binding.playButton.soundClickListener {
            viewModel.startGame()
        }
    }
}