package com.imangel.kotnames

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.imangel.kotnames.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.imangel.kotnames.GameControl as gc

class MainActivity : AppCompatActivity() {

    lateinit var mycontext: Context
    private val gameControl = gc
    private val adapter = Adapter ({gameControl.onCardTouch(it, mycontext)},
        {setScoreText()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mycontext = this.applicationContext
        btnEndTurn.setOnClickListener {
            if (gameControl.gameState != gc.GAME_OVER) gameControl.changeTurn(this)
            setScoreText()
        }
        setScoreText()

        adapter.setHasStableIds(true)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            recycler.layoutManager = GridLayoutManager(this, 5)
        recycler.adapter = adapter
        updateCards()
    }

    fun setScoreText() {
        txtBlueScore.text = (gameControl.blueCards - gameControl.blueUp).toString()
        txtRedScore.text = (gameControl.redCards - gameControl.redUp).toString()
        txtTurn.apply {
            when (gameControl.gameState) {
                gc.GAME_OVER -> {
                    when (gameControl.hasWon) {
                        gc.BLUE_TEAM -> text = "Blue team won!"
                        gc.RED_TEAM -> text = "Red team won!"
                    }
                    setTextColor(Color.parseColor("#000000"))
                }
                else -> {
                    when (gameControl.turn) {
                        gc.BLUE_TEAM -> {
                            text = "Blue's turn"
                            setTextColor(Color.parseColor("#2196F3"))
                        }
                        else -> {
                            text = "Red's turn"
                            setTextColor(Color.parseColor("#D81818"))
                        }
                    }
                }
            }
        }
    }

    private fun updateCards() {
        adapter.items.clear()
        adapter.items.addAll(gameControl.cards)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mainNewGame -> {
                gameControl.newGame(mycontext)
                setScoreText()
                updateCards()
            }
            R.id.mainKeycard -> {
                gc.keycardOn = !gc.keycardOn
                adapter.notifyDataSetChanged()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
