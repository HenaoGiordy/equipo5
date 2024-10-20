package com.univalle.equipo5.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.univalle.equipo5.databinding.DialogDeleteChallengeBinding

class DeleteChallengeDialog(
    private val challengeDescription: String,
    private val onConfirm: () -> Unit // Función lambda que se ejecuta al presionar "SI"
) : DialogFragment() {

    private var _binding: DialogDeleteChallengeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usar ViewBinding para inflar el layout
        _binding = DialogDeleteChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar la descripción del reto
        binding.tvChallengeDescription.text = challengeDescription

        // Acción al hacer clic en el botón "NO"
        binding.tvNo.setOnClickListener {
            dismiss()  // Cierra el diálogo
        }

        // Acción al hacer clic en el botón "SI"
        binding.tvYes.setOnClickListener {
            onConfirm()  // Llama a la función de confirmación
            dismiss()  // Cierra el diálogo
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
