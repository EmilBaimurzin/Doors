package com.lucmon.keyk.game.ui.game

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.lucmon.keyk.R
import com.lucmon.keyk.databinding.FragmentDoorGameBinding
import com.lucmon.keyk.game.domain.game.DoorState
import com.lucmon.keyk.game.ui.bonus.DialogViewModel
import com.lucmon.keyk.game.ui.other.ViewBindingFragment
import com.lucmon.keyk.library.library.getVolumeState
import com.lucmon.keyk.library.library.increaseBalance
import com.lucmon.keyk.library.library.random
import com.lucmon.keyk.library.library.soundClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class FragmentDoorGame : ViewBindingFragment<FragmentDoorGameBinding>(FragmentDoorGameBinding::inflate) {
    private val viewModel: DoorGameViewModel by viewModels()
    private val bonusGameViewModel: DialogViewModel by activityViewModels()
    private val args: FragmentDoorGameArgs by navArgs()
    private var bid = 0L
    private val sp: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("SHARED_PREFS", MODE_PRIVATE)
    }
    private lateinit var doorSound: MediaPlayer
    private lateinit var loseSound: MediaPlayer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bid = args.bid
        bonusGameViewModel.winCallback = {
            viewModel.addLive()
            findNavController().popBackStack(R.id.fragmentGame, false)
        }
        doorSound = MediaPlayer.create(requireContext(), R.raw.sound_opening_door)
        loseSound = MediaPlayer.create(requireContext(), R.raw.sound_over)
        binding.collectButton.soundClickListener(::collect)
        viewModel.list.observe(viewLifecycleOwner) {
            createList()
        }
        binding.biggestWinTextView.text = sp.getLong("BIGGEST_WIN", 0).toString()
        viewModel.attempts.observe(viewLifecycleOwner) {
            binding.attemptsContainer.removeAllViews()
            repeat(it) {
                val attemptView = LottieAnimationView(requireContext())
                val layoutParams = LinearLayout.LayoutParams(dpToPx(26), dpToPx(26))
                layoutParams.setMargins(dpToPx(2), dpToPx(0), dpToPx(2), dpToPx(0))
                attemptView.layoutParams = layoutParams
                attemptView.setAnimation(R.raw.heart)
                attemptView.loop(true)
                attemptView.playAnimation()
                binding.attemptsContainer.addView(attemptView)
            }
        }
        viewModel.coefficient.observe(viewLifecycleOwner) {
            val rate = String.format("%.2f", it).replace(",", ".").toDouble()
            binding.coefficientTextView.text = "$rate" + "x"

            if (rate == 1.0) {
                binding.winningsTextView.text = "0"
            } else {
                binding.collectButton.visibility = View.VISIBLE
                binding.winningsTextView.text = (bid * rate).toInt().toString()
            }
        }
    }

    private fun createList() {
        val size = viewModel.list.value!!.size
        when {
            size <= 5 -> addViews(1, binding.row1, viewModel.list.value!!)
            size >= 6 -> {
                val listForRow1 = mutableListOf<DoorState>()
                val listForRow2 = mutableListOf<DoorState>()
                viewModel.list.value!!.forEachIndexed { index, door ->
                    if (index <= 4) {
                        listForRow1.add(door)
                    } else {
                        listForRow2.add(door)
                    }
                }
                addViews(2, binding.row1, listForRow1)
                addViews(2, binding.row2, listForRow2)
            }
        }
    }

    private fun addViews(rowsAmount: Int, row: LinearLayout, doors: MutableList<DoorState>) {
        val size = when (rowsAmount) {
            1 -> 180
            else -> 90
        }

        doors.forEach { door ->
            val doorView = ImageView(requireContext())
            var layoutParams = LinearLayout.LayoutParams(dpToPx(size) / 2, dpToPx(size))
            if (door.isOpened) {
                layoutParams = LinearLayout.LayoutParams(((dpToPx(size) / 2) * 1.0).toInt(), dpToPx(size))
            }
            layoutParams.setMargins(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6))
            doorView.layoutParams = layoutParams
            setImage(door, doorView)
            doorView.setOnClickListener { view -> clickListener(door, view) }
            row.addView(doorView)
        }
    }

    private fun clickListener(door: DoorState, view: View) {
        if (viewModel.canClick && !door.isOpened) {
            if (getVolumeState(sp)) {
                doorSound.start()
            }
            viewModel.canClick = false
            view as ImageView
            door.isOpened = true
            lifecycleScope.launch {
                delay(300)
                setImage(door, view)
                val layoutParams =
                    LinearLayout.LayoutParams((view.width * 1.0).toInt(), view.height)
                layoutParams.setMargins(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6))
                view.layoutParams = layoutParams
                delay(1000)
                checkWin(door.doorValue)
                viewModel.canClick = true

            }
        }
    }

    private fun collect(view: View) {
        val winnings = (bid * viewModel.coefficient.value!!).toLong()
        when ((1..5).random()) {
            2 -> {
                findNavController().navigate(
                    FragmentDoorGameDirections.actionFragmentGameToFragmentRPS(
                        winnings
                    )
                )
            }
            else -> {
                sp.edit().putLong("LAST_WIN", winnings).apply()
                if (winnings > sp.getLong("BIGGEST_WIN", 0)) sp.edit()
                    .putLong("BIGGEST_WIN", winnings).apply()
                increaseBalance(sp, winnings)
                Toast.makeText(requireContext(), "Your winnings are $winnings", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }

    }

    private fun checkWin(value: Boolean) {
        if (value) {
            binding.row1.removeAllViews()
            binding.row2.removeAllViews()
            viewModel.increaseRate()
            viewModel.next()
        } else {
            lifecycleScope.launch {
                viewModel.minusAttempt()
                delay(100)
                if (viewModel.attempts.value == 0) {
                    if (1 random 4 == 1) {
                        findNavController().navigate(R.id.action_fragmentGame_to_dialogBonusGame)
                    } else {
                        if (getVolumeState(sp)) {
                            loseSound.start()
                        }
                         findNavController().navigate(R.id.action_fragmentGame_to_dialogLose)
                    }
                }
            }
        }
    }

    private fun setImage(door: DoorState, doorView: ImageView) {
        if (!door.isOpened) {
            doorView.setImageResource(R.drawable.img_door_closed)
        } else {
            if (door.doorValue) doorView.setImageResource(R.drawable.img_door_character) else doorView.setImageResource(
                R.drawable.img_door_empty
            )
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density: Float = requireContext().resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }
}

