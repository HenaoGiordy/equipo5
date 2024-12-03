package com.univalle.equipo5.view.fragment

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.univalle.equipo5.R
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.univalle.equipo5.databinding.FragmentHomeMainBinding
import android.os.CountDownTimer
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.univalle.equipo5.databinding.DialogRetoBinding
import com.univalle.equipo5.view.LoginActivity
import com.univalle.equipo5.viewModel.ChallengeViewModel
import com.univalle.equipo5.viewModel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var backgroundMusicPlayer: MediaPlayer? = null
    private var bottleSpinPlayer: MediaPlayer? = null
    private var isSoundOn: Boolean = true
    private lateinit var binding: FragmentHomeMainBinding
    private var countDownTimer: CountDownTimer? = null
    // Variable para recordar si la música estaba sonando antes del giro
    private var wasPlayingBeforeSpin: Boolean = false
    // Ángulo en el que se detuvo la botella anteriormente
    private var currentAngle = 0f
    private lateinit var challengeViewModel:ChallengeViewModel
    private val pokemonViewModel: PokemonViewModel by viewModels()

    // Array con los IDs de los sonidos de botella
    private val bottleSpinSounds = arrayOf(
        R.raw.bottle_spin_1,
        R.raw.bottle_spin_2,
        R.raw.bottle_spin_3,
        R.raw.bottle_spin_4,
        R.raw.bottle_spin_5,
        R.raw.bottle_spin_6,
        R.raw.bottle_spin_7
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundMusicPlayer = MediaPlayer.create(requireContext(), R.raw.soundapp)
        backgroundMusicPlayer?.isLooping = true
        backgroundMusicPlayer?.start()
        challengeViewModel = ViewModelProvider(this)[ChallengeViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout usando DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.customToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        // Configurar el botón parpadeante
        startBlinkingButton()

        // Configuración del contador regresivo en el centro de la botella
        val countdownText = binding.countdownText
        // startCountdownTimer(countdownText)

        // Configurar el evento del botón para girar la botella
        binding.blinkingButton.setOnClickListener {
            // Ocultar el botón y detener el parpadeo
            it.clearAnimation()
            it.visibility = View.INVISIBLE
            startBottleSpin(binding.bottleImage)
            startCountdownTimer(countdownText)

        }

        binding.logout.setOnClickListener {

            logoutUser() // Llama directamente a la función para cerrar sesión
        }

        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)

        binding.sound.setOnClickListener {
            it.startAnimation(scaleAnimation)
            updateSoundState()
            /*if (isSoundOn) {
                backgroundMusicPlayer?.pause()
                binding.sound.setImageResource(R.drawable.nosound)
            } else {
                backgroundMusicPlayer?.start()
                binding.sound.setImageResource(R.drawable.sound)
            }
            isSoundOn = !isSoundOn*/
        }

        binding.instructions.setOnClickListener {
            it.startAnimation(scaleAnimation)
            saveSoundState()
            it.postDelayed({
                findNavController().navigate(R.id.action_homeMain_to_instructions)
            }, 200)

        }

        binding.add.setOnClickListener {
            it.startAnimation(scaleAnimation)
            saveSoundState()
            it.postDelayed({
                findNavController().navigate(R.id.action_homeMain_to_challenge)
            }, 200) // retraso para ver la animación de toque
        }


        binding.share.setOnClickListener { it ->
            it.startAnimation(scaleAnimation)
            saveSoundState()
            val shareTitle = "App pico botella"
            val shareSlogan = "Solo los valientes lo juegan !!"
            val shareUrl = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp"
            val shareContent = "$shareTitle\n$shareSlogan\n$shareUrl"

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareContent)
            }


            // Lista de paquetes permitidos
            val allowedPackages = listOf(
                "com.whatsapp",
                "com.facebook.katana",
                "com.twitter.android",
                "com.instagram.android",
                "com.zhiliaoapp.musically",
                "com.ss.android.ugc.trill"
            )

            val packageManager = requireContext().packageManager
            val resolveInfoList = packageManager.queryIntentActivities(shareIntent, 0)

            val filteredIntents = resolveInfoList
                .filter { resolveInfo ->
                    allowedPackages.contains(resolveInfo.activityInfo.packageName)
                }
                .map { resolveInfo ->
                    Intent(shareIntent).apply {
                        setPackage(resolveInfo.activityInfo.packageName)
                    }
                }

            if (filteredIntents.isNotEmpty()) {
                val chooserIntent = Intent.createChooser(
                    filteredIntents[0],
                    "Compartir vía"
                )

                val remainingIntents = filteredIntents.subList(1, filteredIntents.size).toTypedArray()
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, remainingIntents)

                startActivity(chooserIntent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se encontraron aplicaciones para compartir",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.rate.setOnClickListener {
            it.startAnimation(scaleAnimation)
            saveSoundState()
            it.postDelayed({
                findNavController().navigate(R.id.action_homeMain_to_rate)
            }, 200)
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

    // Función para reproducir un sonido aleatorio de botella girando
    private fun playRandomBottleSpinSound() {
        try {
            // Liberar el MediaPlayer anterior si existe
            bottleSpinPlayer?.release()

            // Seleccionar un sonido aleatorio del array
            val randomSound = bottleSpinSounds[Random.nextInt(bottleSpinSounds.size)]

            // Crear y configurar el nuevo MediaPlayer
            bottleSpinPlayer = MediaPlayer.create(requireContext(), randomSound)
            bottleSpinPlayer?.setOnCompletionListener { mp ->
                mp.release()
                bottleSpinPlayer = null
            }

            // Reproducir solo si el sonido está activado
//            if (isSoundOn) {
                bottleSpinPlayer?.start()
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logoutUser() {
        val sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Borra todos los datos de sesión
        editor.apply()

        // Redirigir al LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpia el back stack
        startActivity(intent)
        requireActivity().finish() // Finaliza la actividad actual
    }

    // Función modificada para hacer girar la botella
    private fun startBottleSpin(bottleImage: ImageView) {
        val randomAngle = Random.nextInt(360) + 720
        val newAngle = currentAngle + randomAngle

        // Guardar el estado de la música y pausarla si está sonando
        wasPlayingBeforeSpin = isSoundOn && backgroundMusicPlayer?.isPlaying == true
        if (wasPlayingBeforeSpin) {
            backgroundMusicPlayer?.pause()
        }

        // Reproducir el sonido de la botella
        playRandomBottleSpinSound()

        val rotateAnimation = RotateAnimation(
            currentAngle, newAngle,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = Random.nextLong(3000, 5000)
        rotateAnimation.fillAfter = true
        bottleImage.startAnimation(rotateAnimation)

        currentAngle = newAngle % 360

        startCountdownTimer(binding.countdownText)

        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {
                startBlinkingButton()
                bottleSpinPlayer?.release()
                bottleSpinPlayer = null

                // Reanudar la música de fondo si estaba sonando antes
                if (wasPlayingBeforeSpin && isSoundOn) {
                    backgroundMusicPlayer?.start()
                }

                showCustomDialog()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun showCustomDialog() {
        val dialogBinding = DataBindingUtil.inflate<DialogRetoBinding>(
            layoutInflater, R.layout.dialog_reto, null, false
        )

        // Crear y mostrar el diálogo
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        // Hacer el fondo transparente
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        viewLifecycleOwner.lifecycleScope.launch {
            val imagenUrl:String = pokemonViewModel.fetchPokemons()
            val imagenUrlHttps = imagenUrl.replaceFirst("http", "https")

            Picasso.get()
                .load(imagenUrlHttps) // Imagen en caso de error opcional
                .into(dialogBinding.pokemonImageView)

            challengeViewModel.getRandomChallenge { challenge ->
                if (challenge != null) {
                    dialogBinding.dialogMessage.text = challenge.description
                } else {
                    dialogBinding.dialogMessage.text = "No hay desafíos disponibles."
                }
            }
        }

        // Configurar el botón "Aceptar" para cerrar el diálogo
        dialogBinding.dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Mostrar el diálogo
        alertDialog.show()
    }

    // Modificar el manejo del botón de sonido
    private fun updateSoundState() {
        if (isSoundOn) {
            backgroundMusicPlayer?.pause()
            bottleSpinPlayer?.pause()
            binding.sound.setImageResource(R.drawable.nosound)
        } else {
            // Solo reanudar la música si no hay un giro en proceso
            if (bottleSpinPlayer == null) {
                backgroundMusicPlayer?.start()
            }
            binding.sound.setImageResource(R.drawable.sound)
        }
        isSoundOn = !isSoundOn
    }

    // Función para iniciar el contador regresivo
    private fun startCountdownTimer(countdownText: TextView) {
        countDownTimer = object : CountDownTimer(4000, 1000) {
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
        backgroundMusicPlayer?.pause()
    }


    override fun onStart() {
        super.onStart()
        isSoundOn = getSoundState()

        if (isSoundOn) {
            backgroundMusicPlayer?.start()
            binding.sound.setImageResource(R.drawable.sound)
        } else {
            backgroundMusicPlayer?.pause()
            binding.sound.setImageResource(R.drawable.nosound)
        }
        val sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)

        if (email == null) {
            // Redirigir al LoginActivity si no hay sesión
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onStop() {
        super.onStop()
        saveSoundState() // Guardamos el estado en caso de salir a segundo plano
    }

    // Función para guardar el estado del sonido
    private fun saveSoundState() {
        val sharedPreferences = requireActivity().getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isSoundOn", isSoundOn)
        editor.apply()
    }


    private fun getSoundState(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isSoundOn", true) // Sonido activado por defecto
    }

    override fun onResume() {
        super.onResume()
        isSoundOn = getSoundState()
        if (isSoundOn) {
            backgroundMusicPlayer?.start()
            binding.sound.setImageResource(R.drawable.sound)
        } else {

            backgroundMusicPlayer?.pause()
            binding.sound.setImageResource(R.drawable.nosound)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isSoundOn", isSoundOn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel() // Cancela el temporizador
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundMusicPlayer?.release()
        backgroundMusicPlayer = null
    }
}