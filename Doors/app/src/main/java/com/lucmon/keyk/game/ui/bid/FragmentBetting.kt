package com.lucmon.keyk.game.ui.bid

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.FragmentBettingBinding
import com.lucmon.keyk.game.ui.other.ViewBindingFragment
import com.lucmon.keyk.library.library.balance
import com.lucmon.keyk.library.library.increaseBalance
import com.lucmon.keyk.library.library.shortToast
import com.lucmon.keyk.library.library.soundClickListener

class FragmentBetting : ViewBindingFragment<FragmentBettingBinding>(FragmentBettingBinding::inflate) {
    private val viewModel: BettingViewModel by viewModels()
    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (balance(sharedPreferences) == 0L) sharedPreferences.edit().putLong("BALANCE", 5000L)
            .apply()
        binding.bidSlider.valueTo = balance(sharedPreferences).toFloat()
        if (balance(sharedPreferences) < 100) viewModel.changeBidValue(0)
        binding.bidSlider.value =
            if (balance(sharedPreferences) > 100) viewModel.bid.value!!.toFloat() else 0f

        binding.balanceTextView.text = "Balance: " + balance(sharedPreferences).toString()
        binding.lastWinTextView.text =
            "Last Win: " + sharedPreferences.getLong("LAST_WIN", 0).toString()

        binding.backButton.soundClickListener {
            findNavController().popBackStack()
        }

        viewModel.bid.observe(viewLifecycleOwner) {
            binding.bidTextView.text = it.toString()
        }

        binding.bidSlider.addOnChangeListener { _, value, _ ->
            viewModel.changeBidValue(value.toLong())
        }

        binding.howToPlayButton.soundClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_fragmentMenu_to_fragmentRules)
        }
        binding.confirmButton.soundClickListener {
            val bid = viewModel.bid.value!!
            if (bid <= balance(sharedPreferences)) {
                if (bid != 0L) {
                    increaseBalance(sharedPreferences, -bid)
                    place()
                } else {
                    shortToast(requireContext(), "Value must not be 0")
                }
            } else {
                shortToast(requireContext(), "Not enough money")
            }
        }

    }

    private fun place() {
        MediaPlayer.create(requireContext(), R.raw.sound_click).start()
        findNavController().navigate(
            FragmentBettingDirections.actionFragmentBidToFragmentGame(
                viewModel.bid.value!!
            )
        )
    }
}