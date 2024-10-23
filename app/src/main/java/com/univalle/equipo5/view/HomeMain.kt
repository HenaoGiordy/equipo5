package com.univalle.equipo5.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.univalle.equipo5.R
import com.univalle.equipo5.databinding.FragmentHomeMainBinding
import kotlin.random.Random

class HomeMain : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    private var isSoundOn: Boolean = true
    private var _binding: FragmentHomeMainBinding? = null
    private val binding get() = _binding!!

    // Ángulo en el que se detuvo la botella anteriormente
    private var currentAngle = 0f

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
        _binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el toolbar
        val toolbar = binding.customToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        // Configurar el botón parpadeante
        startBlinkingButton()

        // Configurar el evento del botón para girar la botella
        binding.blinkingButton.setOnClickListener {
            // Ocultar el botón y detener el parpadeo
            it.clearAnimation()
            it.visibility = View.INVISIBLE
            startBottleSpin(binding.bottleImage)
        }

        // Configuración del contador regresivo en el centro de la botella
        val countdownText = binding.countdownText
        startCountdownTimer(countdownText)

        binding.sound.setImageResource(R.drawable.sound)
        binding.sound.setOnClickListener {
            if (isSoundOn) {
                mediaPlayer?.pause()
                binding.sound.setImageResource(R.drawable.nosound)
            } else {
                mediaPlayer?.start()
                binding.sound.setImageResource(R.drawable.sound)
            }
            isSoundOn = !isSoundOn
        }
    }

    // Función para hacer parpadear el botón
    private fun startBlinkingButton() {
        val blinkAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 500 // Velocidad del parpadeo
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        binding.blinkingButton.startAnimation(blinkAnimation)
    }

    // Función para hacer girar la botella
    private fun startBottleSpin(bottleImage: ImageView) {
        val randomAngle = Random.nextInt(360) + 720 // Rotación aleatoria
        val newAngle = currentAngle + randomAngle

        val rotateAnimation = RotateAnimation(
            currentAngle, newAngle,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = Random.nextLong(3000, 5000)
        rotateAnimation.fillAfter = true
        bottleImage.startAnimation(rotateAnimation)

        currentAngle = newAngle % 360

        // Iniciar el contador regresivo
        startCountdownTimer(binding.countdownText)

        // Reaparecer el botón al finalizar la cuenta regresiva
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startBlinkingButton()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    // Función para iniciar el contador regresivo
    private fun startCountdownTimer(countdownText: TextView) {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdownText.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                countdownText.text = "0"
                binding.blinkingButton.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
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
