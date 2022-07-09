package com.imangel.kotnames

fun setColor(card: Card): Int {
    return when (card.color) {
        GameControl.BLUE -> R.drawable.card_blue
        GameControl.RED -> R.drawable.card_red
        GameControl.BLACK -> R.drawable.card_black
        else -> R.drawable.card_neutral_touched
    }
}