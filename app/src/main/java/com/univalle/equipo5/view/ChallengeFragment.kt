package com.univalle.equipo5.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univalle.equipo5.R
import com.univalle.equipo5.databinding.AddChallengeBinding
import com.univalle.equipo5.databinding.EditChallengeBinding
import com.univalle.equipo5.databinding.FragmentChallengeBinding
import com.univalle.equipo5.view.adapter.ChallengeAdapter
import data.entities.Challenge


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Challenge.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChallengeFragment : Fragment() {
    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    // Mueve la lista de retos aquí
    private val challengeList = mutableListOf<Challenge>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout usando DataBinding
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la lista de retos
        challengeList.add(Challenge(1,"Reto 1: Disfruta de una cerveza"))
        challengeList.add(Challenge(2,"Reto 2: Prueba una cerveza nueva"))

        // Configura tu RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.adapter = ChallengeAdapter(challengeList, { challenge ->
            showDeleteDialog(challenge) // Llama al diálogo al hacer clic en eliminar
        }, { challenge ->
            showEditChallengeDialog(challenge) // Llama al diálogo al hacer clic en editar
        })

        // Animación para el botón de agregar reto
        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
        binding.addChallenge.setOnClickListener {
            it.startAnimation(scaleAnimation)
            showAddChallengeDialog() // Mostrar el cuadro de diálogo aquí
        }

        binding.backButton.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // vuelve a homeMain
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
            val newChallenge = bindingDialog.etChallenge.text.toString()
            if (newChallenge.isNotEmpty()) {
                challengeList.add(Challenge(
                    1,
                    description = TODO()
                ))
                binding.recyclerView.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    // Función para mostrar el cuadro de diálogo de Eliminar
    private fun showDeleteDialog(challenge: Challenge) {
        val dialog = DeleteChallengeDialog(challenge.description) {
            // Aquí se elimina el reto de la lista
            Toast.makeText(context, "Reto eliminado: ${challenge.description}", Toast.LENGTH_SHORT).show()
            // Lógica para eliminar el reto de la lista o base de datos
        }
        dialog.isCancelable = false
        dialog.show(parentFragmentManager, "DeleteChallengeDialog")
    }

    private fun showEditChallengeDialog(Challenge: Challenge) {
        // Usa DataBinding para inflar el layout del diálogo
        val bindingDialog = EditChallengeBinding.inflate(LayoutInflater.from(requireContext()))

        // Crear el diálogo y evitar que se cierre al tocar fuera
        val dialog = AlertDialog.Builder(requireContext())
            .setView(bindingDialog.root) // Poner la vista desde DataBinding
            .setCancelable(false)        // Evitar que se cierre al tocar fuera del diálogo
            .create()

        // Configurar el EditText con la descripción actual
        bindingDialog.etChallenge.setText(Challenge.description)

        // Configuración del botón Cancelar
        bindingDialog.btnEditCancel.setOnClickListener {
            dialog.dismiss() // Cerrar el diálogo al cancelar
        }

        // Configuración del botón Guardar
        bindingDialog.btnEditSave.setOnClickListener {
            val updatedChallenge = bindingDialog.etChallenge.text.toString()
            if (updatedChallenge.isNotEmpty()) {
                // Actualiza el elemento en la lista
                challengeList[challengeList.indexOf(Challenge)] = Challenge(
                    1,
                    description = TODO()
                ) // Actualiza el desafío en la lista
                binding.recyclerView.adapter?.notifyDataSetChanged() // Actualiza el RecyclerView
                dialog.dismiss() // Cerrar el diálogo después de guardar
            }
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChallengeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}