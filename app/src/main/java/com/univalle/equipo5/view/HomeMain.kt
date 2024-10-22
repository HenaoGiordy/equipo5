package com.univalle.equipo5.view

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

class HomeMain : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    private var isSoundOn: Boolean = true
    private var _binding: FragmentHomeMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.soundapp)
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

        val toolbar = binding.customToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)

        binding.sound.setOnClickListener {
            it.startAnimation(scaleAnimation)
            if (isSoundOn) {
                mediaPlayer?.pause()
                binding.sound.setImageResource(R.drawable.nosound)
            } else {
                mediaPlayer?.start()
                binding.sound.setImageResource(R.drawable.sound)
            }
            isSoundOn = !isSoundOn
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

    private fun saveSoundState() {
        val sharedPreferences = requireActivity().getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isSoundOn", isSoundOn)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onStop() {
        super.onStop()
        saveSoundState()
    }

    override fun onStart() {
        super.onStart()
        val isAppRunning = getSoundState()
        isSoundOn = isAppRunning

        if (isSoundOn) {
            mediaPlayer?.start()
            binding.sound.setImageResource(R.drawable.sound)
        } else {
            mediaPlayer?.pause()
            binding.sound.setImageResource(R.drawable.nosound)
        }
    }



    private fun getSoundState(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isSoundOn", true) // Valor por defecto es true (sonido encendido)
    }

    override fun onResume() {
        super.onResume()
        isSoundOn = getSoundState()
        if (isSoundOn) {
            mediaPlayer?.start()
            binding.sound.setImageResource(R.drawable.sound)
        } else {
            mediaPlayer?.pause()
            binding.sound.setImageResource(R.drawable.nosound)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isSoundOn", isSoundOn)
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


