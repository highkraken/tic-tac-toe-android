package com.example.tic_tac_toe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tic_tac_toe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        binding.startButton.setOnClickListener { v ->
            val player1 = binding.player1ET.text.toString().ifEmpty { "Player 1" }
            val player2 = binding.player2ET.text.toString().ifEmpty { "Player 2" }
            val idRG = binding.numRounds.checkedRadioButtonId
            var rounds = 1
            if (idRG != -1) rounds = maxOf(rounds, (activity as AppCompatActivity).findViewById<RadioButton>(idRG).text.toString().toInt())
            if (binding.numRoundET.text.isNotEmpty()) rounds = maxOf(rounds, binding.numRoundET.text.toString().toInt()).coerceAtMost(21)
            v.findNavController().navigate(HomeFragmentDirections.actionLaunchGameFragmentToGameFragment(
                player1, player2, rounds
            ))
            binding.player1ET.text?.clear()
            binding.player2ET.text?.clear()
            binding.numRoundET.text?.clear()
            binding.numRounds.check(binding.rb1.id)
        }
        return binding.root
    }
}