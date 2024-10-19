package com.univalle.equipo5.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.univalle.equipo5.R

/**
 * A simple [Fragment] subclass.
 * Use the [HomeMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeMain : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    private var isSoundOn: Boolean = true

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
        val rootView = inflater.inflate(R.layout.fragment_home_main, container, false)

        val scaleAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_animation)

        val rateIcon: ImageView = rootView.findViewById(R.id.rate)
        val soundIcon: ImageView = rootView.findViewById(R.id.sound)
        val instructionsIcon: ImageView = rootView.findViewById(R.id.instructions)
        val addIcon: ImageView = rootView.findViewById(R.id.add)
        val shareIcon: ImageView = rootView.findViewById(R.id.share)

        // Asignar animación y funcionalidad a cada ícono
        rateIcon.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
        }

        soundIcon.setImageResource(R.drawable.sound)

        soundIcon.setOnClickListener {
            it.startAnimation(scaleAnimation)
            if (isSoundOn) {
                // Si el sonido está encendido, se apaga y cambia el ícono
                mediaPlayer?.pause()
                soundIcon.setImageResource(R.drawable.nosound) // Cambia al ícono de sonido apagado
            } else {
                // Si el sonido está apagado, se enciende y cambia el ícono
                mediaPlayer?.start()
                soundIcon.setImageResource(R.drawable.sound) // Cambia al ícono de sonido encendido
            }
            // Cambia el estado del sonido
            isSoundOn = !isSoundOn
        }

        instructionsIcon.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la fragment Instructions
            // findNavController().navigate(R.id.fragmentInstructions)
        }

        addIcon.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
        }

        shareIcon.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
        }

        return rootView
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular el Toolbar desde el fragment
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.custom_toolbar)

        // Configura el toolbar como la ActionBar de la actividad
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeMain().apply {

            }
    }
}