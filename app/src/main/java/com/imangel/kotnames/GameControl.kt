package com.imangel.kotnames

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import kotlin.random.Random

object GameControl {

    const val BLUE_TEAM = 0
    const val RED_TEAM = 1
    const val NEUTRAL = 0
    const val BLUE = 1
    const val RED = 2
    const val BLACK = 3
    private const val COLUMNS = 5
    private const val ROWS = 5
    private const val GAME_ON = 0
    const val GAME_OVER = 1

    var keycardOn = false
    var hasWon = 2
    var turn = BLUE_TEAM //Turno actual
    var blueUp = 0 //Azules levantadas
    var redUp = 0 //Rojas levantadas
    var blueCards = 0 //Cartas azules en el tablero
    var redCards = 0 //Rojas en el tablero
    private val keycard = mutableListOf<Int>() //Lista con los colores
    private val board = mutableListOf<String>() //Lista con las palabras
    var gameState = GAME_ON
    val cards = mutableListOf<Card>() //Cartas del tablero
    private val words = mutableListOf<String>()

    fun newGame(context: Context) {
        gameState = GAME_ON
        hasWon = 2
        turn = Random.nextInt(2)
        blueUp = 0
        redUp = 0

        when (turn) {
            BLUE_TEAM -> {
                blueCards = 9
                redCards = 8
            }
            RED_TEAM -> {
                blueCards = 8
                redCards = 9
            }
        }


        if (words.isEmpty()) {
            val gson = Gson()
            val jsonFileString = getJsonDataFromAsset(context, "Words.json")
            val wordsType = object : TypeToken<List<MyWord>>(){}.type
            val mywords: List<MyWord> = gson.fromJson(jsonFileString, wordsType)

            mywords.forEach{
                words.add(it.Word)
                //Log.i("Palabrejas", it.Word)
            }
        }



        newKeycard()
        newBoard()
        cards.apply{
            clear()
            for (i in (0..keycard.size-1)) {
                val card = Card(board[i], keycard[i], i, false)
                add(card)
            }
        }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun newKeycard() {
        keycard.apply {
            clear() //Vaciar la keycard para nueva partida
            add(BLACK) //Negra
            for (i in (1..7)) add(NEUTRAL) //Neutrales
            for (i in (1..blueCards)) add(BLUE) //Azules
            for (i in (1..redCards)) add(RED) //Rojas
            shuffle() //Barajar
        }
    }

    private fun newBoard() {
        board.clear() //Vaciar el board para nueva partida
        val taken = mutableListOf<Int>()
        for (i in (1..(COLUMNS * ROWS))) {
            var rnd: Int
            do {
                rnd = Random.nextInt(400) //Encontrar una palabra no cogida antes
            } while (taken.contains(rnd))

            board.add(getWord(rnd)) //Meter esa palabra en el tablero
            //board.add("Test$i")
            taken.add(rnd)
        }
    }

    private fun getWord(id: Int): String {
        //Sacar la palabra con el id recibido de la DB
        return words[id]
    }

    private fun isOver(context: Context): Boolean {
        if (blueUp == blueCards) {
            gameState = GAME_OVER
            hasWon = turn
            Toast.makeText(context, "Blue team won!", Toast.LENGTH_SHORT).show()
            return true
        } else if (redUp == redCards) {
            gameState = GAME_OVER
            hasWon = turn
            Toast.makeText(context, "Red team won!", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    fun changeTurn(context: Context) {
        when (turn) {
            BLUE_TEAM -> {
                turn = RED_TEAM
                Toast.makeText(context, "Red's turn", Toast.LENGTH_SHORT).show()
            }
            RED_TEAM -> {
                turn = BLUE_TEAM
                Toast.makeText(context, "Blue's turn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onCardTouch(card: Card, context: Context) {
        //Log.i("Card", card.word)
        if (!card.touched && gameState != GAME_OVER) { //Si la carta tocada no ha sido tocada previamente
            card.touched = true
            when (card.color) {
                BLUE -> {
                    blueUp++
                    val isOver = isOver(context)
                    if (!isOver && turn != BLUE_TEAM) changeTurn(context)
                }
                RED -> {
                    redUp++
                    val isOver = isOver(context)
                    if (!isOver && turn != RED_TEAM) changeTurn(context)
                }
                NEUTRAL -> {
                    when (turn) {
                        BLUE_TEAM -> {
                            turn = RED_TEAM
                            Toast.makeText(context, "Red's turn", Toast.LENGTH_SHORT).show()
                        }
                        RED_TEAM -> {
                            turn = BLUE_TEAM
                            Toast.makeText(context, "Blue's turn", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> {
                    gameState = GAME_OVER
                    when (turn) {
                        BLUE_TEAM -> {
                            hasWon = RED_TEAM
                            Toast.makeText(context, "Red team won!", Toast.LENGTH_SHORT).show()
                        }
                        RED_TEAM -> {
                            hasWon = BLUE_TEAM
                            Toast.makeText(context, "Blue team won!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}