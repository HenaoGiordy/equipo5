package com.univalle.equipo5.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univalle.equipo5.R
import com.univalle.equipo5.databinding.AddChallengeBinding
import com.univalle.equipo5.databinding.FragmentChallengeBinding
import com.univalle.equipo5.view.adapter.ChallengeAdapter
import com.univalle.equipo5.view.model.ChallengeItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Challenge.newInstance] factory method to
 * create an instance of this fragment.
 */
class Challenge : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        // Crear una lista de datos
        val challengeList = mutableListOf(
            ChallengeItem("Reto 1: Disfruta de una cerveza"),
            ChallengeItem("Reto 2: Prueba una cerveza nueva")
        )

        // Configura tu RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = ChallengeAdapter(challengeList)

        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
        binding.addChallenge.setOnClickListener {
            it.startAnimation(scaleAnimation)
            showAddChallengeDialog(challengeList) // Mostrar el cuadro de diálogo aquí
        }

        binding.backButton.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // vuelve a homeMain
            findNavController().navigate(R.id.action_challenge_to_homeMain)
        }

    }

    private fun showAddChallengeDialog(challengeList: MutableList<ChallengeItem>) {
        // Usa DataBinding para inflar el layout del diálogo
        val bindingDialog = AddChallengeBinding.inflate(LayoutInflater.from(requireContext()))

        // Crear el diálogo
        val dialog = AlertDialog.Builder(requireContext())
            .setView(bindingDialog.root) // Poner la vista desde DataBinding
            .setCancelable(true)
            .create()

        // Configura los elementos de la vista usando el binding
        bindingDialog.btnSave.isEnabled = false
        bindingDialog.btnSave.setBackgroundColor(resources.getColor(R.color.gray)) // Estado inactivo

        // Habilitar o deshabilitar el botón de guardar dinámicamente
        bindingDialog.etChallenge.addTextChangedListener {
            val inputText = bindingDialog.etChallenge.text.toString()
            if (inputText.isNotEmpty()) {
                bindingDialog.btnSave.isEnabled = true
                bindingDialog.btnSave.setBackgroundColor(resources.getColor(R.color.orange)) // Habilitado
            } else {
                bindingDialog.btnSave.isEnabled = false
                bindingDialog.btnSave.setBackgroundColor(resources.getColor(R.color.gray)) // Deshabilitado
            }
        }

        // Configuración del botón Cancelar
        bindingDialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Configuración del botón Guardar
        bindingDialog.btnSave.setOnClickListener {
            val newChallenge = bindingDialog.etChallenge.text.toString()
            if (newChallenge.isNotEmpty()) {
                challengeList.add(ChallengeItem(newChallenge)) // Agregar el reto a la lista
                binding.recyclerView.adapter?.notifyDataSetChanged() // Actualizar el RecyclerView
                dialog.dismiss()
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
            Challenge().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}