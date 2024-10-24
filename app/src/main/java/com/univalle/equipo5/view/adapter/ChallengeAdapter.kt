package com.univalle.equipo5.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.univalle.equipo5.R
import com.univalle.equipo5.databinding.ItemChallengeBinding
import data.entities.Challenge

// ChallengeAdapter.kt
class ChallengeAdapter(
    private val challengeList: List<Challenge>,
    private val onDeleteChallenge: (Challenge) -> Unit,
    private val onEditChallenge: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // Usar DataBinding en el ViewHolder
    class ChallengeViewHolder(val binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        // Inflar usando DataBinding
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemChallengeBinding = DataBindingUtil.inflate(inflater, R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challengeItem = challengeList[position]

        // Vincular el objeto Challenge directamente con el layout usando DataBinding
        holder.binding.challenge = challengeItem  // Establece el objeto en el layout (challenge)

        // Animación y lógica para el botón de editar
        holder.binding.editChallenge.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            it.startAnimation(scaleAnimation)
            onEditChallenge(challengeItem)
        }

        // Animación y lógica para el botón de eliminar
        holder.binding.deleteChallenge.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            it.startAnimation(scaleAnimation)
            onDeleteChallenge(challengeItem)
        }
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }
}

