package com.lucmon.keyk.game.ui.bonus

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.DialogBonusGameWinBinding
import com.lucmon.keyk.game.ui.other.ViewBindingFragment
import com.lucmon.keyk.library.library.soundClickListener

class DialogBonusGameWin :
    ViewBindingFragment<DialogBonusGameWinBinding>(DialogBonusGameWinBinding::inflate) {
    private val viewModel: DialogViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.continueButton.soundClickListener {
            viewModel.winCallback?.invoke()
            findNavController().popBackStack(R.id.fragmentGame, false)
        }
    }
}