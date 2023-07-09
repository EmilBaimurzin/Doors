package com.lucmon.keyk.game.ui.bonus

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.DialogBonusGameBinding
import com.lucmon.keyk.game.domain.adapters.rules.GameRulesAdapter
import com.lucmon.keyk.library.library.ViewBindingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DialogBonusGame : ViewBindingDialog<DialogBonusGameBinding>(DialogBonusGameBinding::inflate) {
    private val viewModel: DialogViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentBid, false)
                true
            } else {
                false
            }
        }
        viewModel.loseCallback = {
            lifecycleScope.launch(Dispatchers.Main) {
                findNavController().popBackStack(R.id.fragmentGame, false)
                findNavController().navigate(R.id.action_fragmentGame_to_dialogLose)
            }
        }
        binding.dialogViewPager.isUserInputEnabled = false
        binding.dialogViewPager.adapter =
            GameRulesAdapter(childFragmentManager,
                lifecycle,
                arrayListOf(DialogBonusGameText(), DialogBonusGameGame(), DialogBonusGameWin()))

        viewModel.currentPage.observe(viewLifecycleOwner) {
            binding.dialogViewPager.currentItem = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.changePage(0)
        viewModel.reset()
    }
}