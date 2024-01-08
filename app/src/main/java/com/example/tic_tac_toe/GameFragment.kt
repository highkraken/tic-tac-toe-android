package com.example.tic_tac_toe

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.tic_tac_toe.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    data class Game(
        val p1Name: String,
        var p1Score: String = "0",
        val p2Name: String,
        var p2Score: String = "0",
        val rounds: Int
    )
    lateinit var game: Game
    private var endGameTriggered = false
    private val trackOfBoard = List(3) { MutableList(3) { '.' } }
    private lateinit var binding: FragmentGameBinding
    private lateinit var board: List<MutableList<ImageView>>

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game, container, false
        )
        val args = GameFragmentArgs.fromBundle(requireArguments())
        (activity as AppCompatActivity).supportActionBar?.title =
            "First to score ${args.numRounds / 2 + 1} wins"
        game = Game(p1Name = args.p1Name, p2Name = args.p2Name, rounds = args.numRounds)
        binding.gameInst = this
        val circleDrawable = R.drawable.circle
        val crossDrawable = R.drawable.cross
        var turn = true
        board = List(3) { MutableList(3) { binding.r1c1 } }
        board[0][1] = binding.r1c2
        board[0][2] = binding.r1c3
        board[1][0] = binding.r2c1
        board[1][1] = binding.r2c2
        board[1][2] = binding.r2c3
        board[2][0] = binding.r3c1
        board[2][1] = binding.r3c2
        board[2][2] = binding.r3c3


        binding.p1Display.text = "${args.p1Name}: "
        binding.p2Display.text = "${args.p2Name}: "
        if (turn) {
            binding.p1Display.setTypeface(null, Typeface.BOLD_ITALIC)
            binding.p1Display.letterSpacing = 0.1f
        }

        for (i in board.indices) {
            for (j in board[0].indices) {
                board[i][j].setOnClickListener {
                    if (trackOfBoard[i][j] == '.') {
                        it.setBackgroundResource(if (turn) circleDrawable else crossDrawable)
                        trackOfBoard[i][j] = if (turn) 'O' else 'X'
                        turn = !turn
                        if (turn) {
                            binding.p1Display.setTypeface(null, Typeface.BOLD_ITALIC)
                            binding.p1Display.letterSpacing = 0.1f
                            binding.p2Display.setTypeface(null, Typeface.NORMAL)
                            binding.p2Display.letterSpacing = 0f
                        }
                        else {
                            binding.p1Display.setTypeface(null, Typeface.NORMAL)
                            binding.p1Display.letterSpacing = 0f
                            binding.p2Display.setTypeface(null, Typeface.BOLD_ITALIC)
                            binding.p2Display.letterSpacing = 0.1f
                        }
                        if (checkEndGame(args.p1Name, args.p2Name)) endGameTriggered = true
                        Log.e("TRACKBOARD", trackOfBoard.toString())
                    }
                }
            }
        }
        return binding.root
    }

    private fun checkEndGame(p1: String, p2: String): Boolean  {
        // row check
        trackOfBoard
            .forEach { row ->
                if (row.all { it == 'O' }) return updateScores("p1")
                if (row.all { it == 'X' }) return updateScores("p2")
            }

        // column check
        (trackOfBoard.indices)
            .map { col -> trackOfBoard.map { it[col] } }
            .forEach { row ->
                if (row.all { it == 'O' })  return updateScores("p1")
                if (row.all { it == 'X' })  return updateScores("p2")
            }

        // primary diagonal check
        val primaryDiagonal = trackOfBoard
            .mapIndexed { index, row ->
                row[index]
            }
        if (primaryDiagonal.all { it == 'O' })  return updateScores("p1")
        if (primaryDiagonal.all { it == 'X' })  return updateScores("p2")

        // secondary diagonal check
        val secondaryDiagonal = trackOfBoard
            .reversed()
            .mapIndexed { index, row ->
                row[index]
            }
        if (secondaryDiagonal.all { it == 'O' })  return updateScores("p1")
        if (secondaryDiagonal.all { it == 'X' })  return updateScores("p2")

        if (trackOfBoard.all { row -> row.all { box -> box != '.' && box != '#' } })  {
            return updateScores("none")
        }
        return false
    }

    private fun updateScores(winner: String): Boolean    {
        for (row in 0..2)   {
            for (ind in 0..2) {
                if (trackOfBoard[row][ind] == '.')  trackOfBoard[row][ind] = '#'
            }
        }
        Log.e("GAMEHAIJI", game.toString())
        if (winner == "p1") {
            game.p1Score = "${game.p1Score.toInt() + 1}"
            binding.p1Score.text = game.p1Score
        }   else if (winner == "p2")    {
            game.p2Score = "${game.p2Score.toInt() + 1}"
            binding.p2Score.text = game.p2Score
        }
        val winnerName = when (winner) {
            "p1" -> game.p1Name
            "p2" -> game.p2Name
            else -> "none"
        }
        showDialog(winnerName, game.p1Score.toInt() > game.rounds / 2 || game.p2Score.toInt() > game.rounds / 2)
        return true
    }

    private fun clearBoard()    {
        board.forEach { row ->
            row.forEach {
                it.setBackgroundResource(R.drawable.empty)
            }
        }
        trackOfBoard.forEach { row ->
            row.forEachIndexed { ind, _ ->
                row[ind] = '.'
            }
        }
    }

    private fun showDialog(winner: String, gameOver: Boolean=false)  {
        val builder = AlertDialog.Builder(activity as AppCompatActivity)
        val title = if (winner == "none") "Round drawn" else "Round Won"
        val message = if (winner == "none") "Ah! No one won the round as it resulted in a draw." else "Hurray! $winner has won the round"
        builder.setTitle(if (!gameOver) title else "Game Over")
        builder.setMessage(message + if (gameOver) " and also the game. Kudos to them!" else ".")
        builder.setPositiveButton(if (!gameOver) "Next Round" else "GG") { _, _ ->
            clearBoard()
            if (gameOver) {
                this.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(
                    game.p1Name, game.p2Name, game.rounds, winner
                ))
            }
        }
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}