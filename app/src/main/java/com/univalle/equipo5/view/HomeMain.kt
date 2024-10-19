package com.univalle.equipo5.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.univalle.equipo5.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeMain : Fragment() {
    // TODO: Rename and change types of parameters
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

        soundIcon.setOnClickListener {
            it.startAnimation(scaleAnimation)
            // Navegar a la siguiente pantalla o realizar otra acción
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular el Toolbar desde el fragment
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.custom_toolbar)

        // Configura el toolbar como la ActionBar de la actividad
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}