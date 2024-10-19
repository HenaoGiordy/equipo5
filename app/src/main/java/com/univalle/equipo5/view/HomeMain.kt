package com.univalle.equipo5.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.univalle.equipo5.R
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.univalle.equipo5.databinding.FragmentHomeMainBinding

class HomeMain : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    private var isSoundOn: Boolean = true
    private var _binding: FragmentHomeMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.soundpokemon)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout usando DataBinding
        _binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el Toolbar desde el fragment usando DataBinding
        val toolbar = binding.customToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        // Asignar animación y funcionalidad a cada ícono usando binding
        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)

        binding.rate.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
        }

        binding.sound.setImageResource(R.drawable.sound)

        binding.sound.setOnClickListener {
            it.startAnimation(scaleAnimation)
            if (isSoundOn) {
                mediaPlayer?.pause()
                binding.sound.setImageResource(R.drawable.nosound) // Cambia al ícono de sonido apagado
            } else {
                mediaPlayer?.start()
                binding.sound.setImageResource(R.drawable.sound) // Cambia al ícono de sonido encendido
            }
            isSoundOn = !isSoundOn
        }

        binding.instructions.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la fragment Instructions
            findNavController().navigate(R.id.action_homeMain_to_instructions)
        }

        binding.add.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
        }

        binding.share.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        // Reinicia el sonido si estaba encendido
        if (isSoundOn) {
            mediaPlayer?.start()
            binding.sound.setImageResource(R.drawable.sound)
        } else {
            mediaPlayer?.pause()
            binding.sound.setImageResource(R.drawable.nosound)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeMain().apply {}
    }
}


