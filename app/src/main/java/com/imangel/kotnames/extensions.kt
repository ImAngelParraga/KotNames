package com.imangel.kotnames

fun setColor(card: Card): Int {
    when (card.color) {
        GameControl.BLUE -> return R.drawable.card_blue
        GameControl.RED -> return R.drawable.card_red
        GameControl.BLACK -> return R.drawable.card_black
        else -> return R.drawable.card_neutral_touched
    }
}