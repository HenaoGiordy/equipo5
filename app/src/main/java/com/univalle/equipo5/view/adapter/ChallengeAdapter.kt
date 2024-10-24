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
    private var challengeList: MutableList<Challenge>, // Usa 'MutableList' para poder modificar la lista
    private val onDeleteChallenge: (Challenge) -> Unit, // Se llamará cuando se haga clic en eliminar
    private val onEditChallenge: (Challenge) -> Unit // Se llamará cuando se haga clic en editar
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // ViewHolder que usa DataBinding
    class ChallengeViewHolder(val binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        // Infla el layout del elemento usando DataBinding
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemChallengeBinding = DataBindingUtil.inflate(inflater, R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challengeItem = challengeList[position]

        // Vincula el objeto Challenge al layout (DataBinding)
        holder.binding.challenge = challengeItem

        // Animación y lógica para el botón de editar
        holder.binding.editChallenge.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            it.startAnimation(scaleAnimation)
            onEditChallenge(challengeItem) // Llama a la función de edición
        }

        // Animación y lógica para el botón de eliminar
        holder.binding.deleteChallenge.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            it.startAnimation(scaleAnimation)
            onDeleteChallenge(challengeItem) // Llama a la función de eliminación
        }
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = challengeList.size

    // Método para actualizar la lista completa de retos
    fun updateChallenges(newChallenges: List<Challenge>) {
        this.challengeList.clear()
        this.challengeList.addAll(newChallenges)
        notifyDataSetChanged() // Refresca todo el RecyclerView
    }

    // Método para actualizar un reto en una posición específica
    fun updateChallengeAtPosition(position: Int, updatedChallenge: Challenge) {
        challengeList[position] = updatedChallenge
        notifyItemChanged(position) // Notifica solo el cambio en la posición actual
    }
}
