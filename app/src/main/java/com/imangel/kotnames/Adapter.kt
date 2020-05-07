package com.imangel.kotnames

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imangel.kotnames.databinding.CardItemBinding
import kotlinx.android.synthetic.main.card_item.view.*

class Adapter(val listener: (Card) -> Unit, val updateUi: () -> Unit) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    val items = ArrayList<Card>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CardItemBinding.bind(view)
        var keycardOn = false

        //Colocar img y texto en recycler dada una carta
        fun bind(card: Card) {
            binding.cardWord.text = card.word
            if (card.touched) {
                if (card.color == GameControl.NEUTRAL) binding.cardWord.setTextColor(Color.parseColor("#009900"))
                else binding.cardWord.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else binding.cardWord.setTextColor(Color.parseColor("#000000"))


            binding.bgCard.setImageResource(
                when (card.touched) {
                    false -> R.drawable.card_neutral
                    true -> {setColor(card)}
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return items[position].pos.toLong()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = items[position]
        holder.bind(card)
        holder.itemView.setOnClickListener {
            if (!card.touched && GameControl.gameState != GameControl.GAME_OVER) {
                holder.itemView.bgCard.setImageResource(setColor(card))
                holder.itemView.cardWord.setTextColor(Color.parseColor("#FFFFFF"))
            }
            listener(card)
            updateUi()
        }
        if (GameControl.keycardOn) {
            holder.itemView.apply {
                bgCard.setImageResource(setColor(card))
                if (card.color == GameControl.NEUTRAL) cardWord.setTextColor(Color.parseColor("#009900"))
                else cardWord.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
    }
}
