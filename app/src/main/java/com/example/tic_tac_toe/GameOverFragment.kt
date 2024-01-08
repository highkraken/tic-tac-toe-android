package com.example.tic_tac_toe

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tic_tac_toe.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    private lateinit var binding: FragmentGameOverBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val context = activity as AppCompatActivity
        val args = GameOverFragmentArgs.fromBundle(requireArguments())
        context.supportActionBar?.title = "${args.winner} won"
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game_over, container, false
        )
        binding.homeBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }
        binding.playAgainBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val editText = EditText(context)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setText("${args.numRounds}")
            builder.setView(editText)
            builder.setTitle("Enter number of rounds")
            builder.setPositiveButton("Play")   { _, _ ->
                this.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment(
                    args.p1Name, args.p2Name, editText.text.toString().toInt()
                ))
            }
            val dialog = builder.create()
            dialog.show()
        }
        val wonRounds = args.numRounds / 2 + 1
        binding.winnerTv.text = "Yay! ${args.winner} won the game\nwith a score of $wonRounds - ${args.numRounds - wonRounds}"
        return binding.root
    }
}