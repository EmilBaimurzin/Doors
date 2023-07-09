package com.lucmon.keyk.game.ui.bonus

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.lucmon.keyk.databinding.DialogBonusGameGameBinding
import com.lucmon.keyk.game.domain.bonus.GameState
import com.lucmon.keyk.game.ui.other.ViewBindingFragment

class DialogBonusGameGame :
    ViewBindingFragment<DialogBonusGameGameBinding>(DialogBonusGameGameBinding::inflate) {
    private val viewModel: DialogViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clickButton.setOnClickListener {
            viewModel.increaseProgress()
        }

        viewModel.gameValue.observe(viewLifecycleOwner) {
            when (it!!) {
                GameState.GAME_HAVE_NOT_STARTED -> {
                    binding.clickButton.isEnabled = false
                }
                GameState.GAME_STARTED -> {
                    binding.clickButton.isEnabled = true
                }
                GameState.GAME_ENDED -> {
                    binding.clickButton.isEnabled = false
                }
            }
        }
        viewModel.progress.observe(viewLifecycleOwner) {
            binding.progress.progress = it
            if (it >= 950) {
                viewModel.win()
            }
        }
        viewModel.tenSecondsTimer.observe(viewLifecycleOwner) {
            binding.tenSecondsTimer.text = it.toString()
            if (viewModel.gameValue.value!! == GameState.GAME_HAVE_NOT_STARTED) {
                binding.tenSecondsTimer.visibility =
                    View.INVISIBLE
            } else {
                binding.tenSecondsTimer.visibility =
                    View.VISIBLE
            }
        }
        viewModel.threeSecondsTimer.observe(viewLifecycleOwner) {
            binding.threeSecondsTimer.text = it.toString()
            if (it == 0) {
                binding.threeSecondsTimer.text = "Click click!"
            }
        }
    }
}