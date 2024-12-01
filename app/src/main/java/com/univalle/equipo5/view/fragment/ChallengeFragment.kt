package com.univalle.equipo5.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univalle.equipo5.R
import com.univalle.equipo5.databinding.AddChallengeBinding
import com.univalle.equipo5.databinding.EditChallengeBinding
import com.univalle.equipo5.databinding.FragmentChallengeBinding
import com.univalle.equipo5.view.DeleteChallengeDialog
import com.univalle.equipo5.view.adapter.ChallengeAdapter
import com.univalle.equipo5.model.Challenge
import com.univalle.equipo5.viewModel.ChallengeViewModel
import kotlinx.coroutines.launch

class ChallengeFragment : Fragment() {
    private lateinit var binding: FragmentChallengeBinding
    private val viewModel: ChallengeViewModel by activityViewModels()

    private lateinit var challengeAdapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura tu RecyclerView
        challengeAdapter = ChallengeAdapter(mutableListOf(), { challenge ->
            showDeleteDialog(challenge)
        }, { challenge ->
            showEditChallengeDialog(challenge)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = challengeAdapter

        // Observar los cambios en la lista de retos desde el ViewModel
        viewModel.challenges.observe(viewLifecycleOwner, { challengeList ->
            // Convertir la lista inmutable a mutable antes de pasarla al adaptador
            challengeAdapter.updateChallenges(challengeList.toMutableList())
        })

        // Animación para el botón de agregar reto
        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
        binding.addChallenge.setOnClickListener {
            it.startAnimation(scaleAnimation)
            showAddChallengeDialog()
        }

        binding.backButton.setOnClickListener {
            it.startAnimation(scaleAnimation)
            findNavController().navigate(R.id.action_challenge_to_homeMain)
        }
    }

    private fun showAddChallengeDialog() {
        val bindingDialog = AddChallengeBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(bindingDialog.root)
            .setCancelable(false)
            .create()

        bindingDialog.btnSave.isEnabled = false
        bindingDialog.btnSave.setBackgroundColor(resources.getColor(R.color.gray))

        bindingDialog.etChallenge.addTextChangedListener {
            val inputText = bindingDialog.etChallenge.text.toString()
            bindingDialog.btnSave.isEnabled = inputText.isNotEmpty()
            bindingDialog.btnSave.setBackgroundColor(
                if (inputText.isNotEmpty()) resources.getColor(R.color.orange) else resources.getColor(R.color.gray)
            )
        }

        bindingDialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog.btnSave.setOnClickListener {
            val newChallengeDescription = bindingDialog.etChallenge.text.toString()
            if (newChallengeDescription.isNotEmpty()) {
                val newChallenge = Challenge(description = newChallengeDescription)
                viewModel.insertChallenge(newChallenge)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showDeleteDialog(challenge: Challenge) {
        val dialog = DeleteChallengeDialog(challenge.description) {
            viewModel.deleteChallenge(challenge)
            Toast.makeText(context, "Reto eliminado: ${challenge.description}", Toast.LENGTH_SHORT).show()
        }
        dialog.isCancelable = false
        dialog.show(parentFragmentManager, "DeleteChallengeDialog")
    }

    private fun showEditChallengeDialog(challenge: Challenge) {
        val bindingDialog = EditChallengeBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(bindingDialog.root)
            .setCancelable(false)
            .create()

        bindingDialog.etChallenge.setText(challenge.description)

        bindingDialog.btnEditCancel.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog.btnEditSave.setOnClickListener {
            val updatedDescription = bindingDialog.etChallenge.text.toString()
            if (updatedDescription.isNotEmpty()) {
                val updatedChallenge = challenge.copy(description = updatedDescription)

                // Llamar a la función suspend desde una corutina
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateChallenge(updatedChallenge)
                    dialog.dismiss()
                }
            } else {
                Toast.makeText(requireContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}
