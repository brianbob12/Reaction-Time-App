package com.example.reactiontime

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var lastTime=-1.0
    private var started=false
    private var listening=false
    private var startTime:LocalDateTime? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.readyButton).setOnClickListener{
            listening=false
            //hide buttons
            view.findViewById<Button>(R.id.button_second).visibility=View.INVISIBLE
            view.findViewById<Button>(R.id.button_second).isClickable=false
            view.findViewById<Button>(R.id.readyButton).visibility=View.INVISIBLE
            view.findViewById<Button>(R.id.readyButton).isClickable=false
            view.findViewById<TextView>(R.id.messageText).visibility=View.INVISIBLE
            view.setBackgroundColor(Color.WHITE)
            //random delay
            started=true
            val handler = Handler()
            handler.postDelayed(object: Runnable{
                private var color:Int = 0
                private var view:View? = null
                operator fun invoke(v:View?, col: Int): Runnable {
                    this.color=col
                    this.view=v
                    return this
                }
                override fun run() {
                    if(started) {
                        if (Random.nextInt(0, 18) == 0) {
                            this.view?.setBackgroundColor(this.color)
                            listening = true
                            startTime=LocalDateTime.now()
                        } else {
                            handler.postDelayed(this, 100)
                        }
                    }
                }

            }(this.getView(),getResources().getColor(R.color.greenDark)),1000)


        }
        //on screen click
        view.setOnClickListener(View.OnClickListener{
            if(listening) {
                //clicking after the green screen
                view.findViewById<Button>(R.id.button_second).visibility=View.VISIBLE
                view.findViewById<Button>(R.id.button_second).isClickable=true
                view.findViewById<Button>(R.id.readyButton).visibility=View.VISIBLE
                view.findViewById<Button>(R.id.readyButton).isClickable=true
                view.findViewById<TextView>(R.id.messageText).visibility=View.VISIBLE
                val score=LocalDateTime.from(startTime).until(LocalDateTime.now(), ChronoUnit.MILLIS)
                view.findViewById<TextView>(R.id.messageText).text="Time:"+score+" miliseconds"
                if((getActivity()as MainActivity).bestTime<0 ||(getActivity()as MainActivity).bestTime>score){
                    (getActivity()as MainActivity).bestTime= score.toDouble()
                    var file= File(context?.filesDir,"bestTime.txt")
                    file.writeText((getActivity()as MainActivity).bestTime.toString())
                }
                listening=false
                started=false
            }
            else if(started){
                //clicking before the greeen screen
                view.setBackgroundColor(Color.RED)
                started=false
                view.findViewById<Button>(R.id.button_second).visibility=View.VISIBLE
                view.findViewById<Button>(R.id.button_second).isClickable=true
                view.findViewById<Button>(R.id.readyButton).visibility=View.VISIBLE
                view.findViewById<Button>(R.id.readyButton).isClickable=true
                view.findViewById<TextView>(R.id.messageText).visibility=View.VISIBLE
                view.findViewById<TextView>(R.id.messageText).text="You cliked too soon"
            }
        })
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
    private fun greenOut(){
        this.getView()?.setBackgroundColor(getResources().getColor(R.color.greenDark))
    }

}