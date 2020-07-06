package com.example.reactiontime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class IntroFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //import best time if needed
        if((getActivity()as MainActivity).bestTime!=-2.0) {
            var file: File? = null
            try {
                file = File(context?.filesDir, "bestTime.txt")
                val raw = file.readText()
                (getActivity()as MainActivity).bestTime=raw.toDouble()
            } catch (e: Exception) {
                //file does not exsist create new file
                (getActivity()as MainActivity).bestTime=-1.0
                file = File(context?.filesDir, "bestTime.txt")
                file.createNewFile()
                file.writeText("-1.0")
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if((getActivity()as MainActivity).bestTime>0){
            view.findViewById<TextView>(R.id.textview_intro).setText("Best Time:"+(getActivity()as MainActivity).bestTime+" miliseconds")
        }
        view.findViewById<Button>(R.id.button_intro).setOnClickListener {
            findNavController().navigate(R.id.action_IntroFragment_to_SecondFragment)
        }
    }
}