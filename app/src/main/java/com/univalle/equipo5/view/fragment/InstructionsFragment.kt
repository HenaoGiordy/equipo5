package com.univalle.equipo5.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.databinding.DataBindingUtil
import com.univalle.equipo5.R
import com.univalle.equipo5.databinding.FragmentInstructionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usamos DataBindingUtil para inflar el layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instructions, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el clic del botón para navegar a HomeMain
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_instructions_to_homeMain)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = InstructionsFragment()
    }
}
