package com.lucmon.keyk.game.ui.game

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.DialogLoseBinding
import com.lucmon.keyk.game.ui.main_menu.FragmentMainMenuDirections
import com.lucmon.keyk.library.library.soundClickListener

class DialogLosing : DialogFragment() {
    private lateinit var binding: DialogLoseBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogLoseBinding.inflate(layoutInflater)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentBid, false)
                true
            } else {
                false
            }
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.leaveButton.soundClickListener {
            findNavController().navigate(R.id.fragmentMenu)
        }

        binding.tryAgainButton.soundClickListener {
            findNavController().navigate(R.id.fragmentMenu)
            findNavController().navigate(FragmentMainMenuDirections.actionFragmentMenuToFragmentBid())
        }
    }
}