package com.univalle.equipo5.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.univalle.equipo5.R
import com.univalle.equipo5.view.model.ChallengeItem

// ChallengeAdapter.kt

class ChallengeAdapter(private val challengeList: List<ChallengeItem>) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // Vista del elemento del RecyclerView
    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconChallenge: ImageView = itemView.findViewById(R.id.iconChallenge)
        val descriptionChallenge: TextView = itemView.findViewById(R.id.descriptionChallenge)
    }

    // Crear nuevas vistas (invocadas por el layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    // Reemplazar el contenido de una vista (invocado por el layout manager)
    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challengeItem = challengeList[position]
        holder.descriptionChallenge.text = challengeItem.description
    }

    // Retornar el tamaño de la lista
    override fun getItemCount(): Int {
        return challengeList.size
    }
}
