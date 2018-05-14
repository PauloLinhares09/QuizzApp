package com.example.henrique.kotlinapplication.fragments


import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

import com.example.henrique.kotlinapplication.R
import com.example.henrique.kotlinapplication.models.Resposta
import kotlinx.android.synthetic.main.fragment_blank.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BlankFragment : Fragment() {

    private val randomNumber = Random()
    private val DIFICULTY = 200
    private var value1: Int? = null
    private var value2: Int? = null
    private var answer: Int? = null
    private var myAnswer: Int? = null
    private var init = false
    private var op = "+"
    private var timer: CountDownTimer? = null
    private var timerNow: Long? = null
    private var listAnswers = ArrayList <Resposta> ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        text_op.text = op
    }


    private fun initViews() {

        radioButtonA.isEnabled = false
        radioButtonB.isEnabled = false
        radioButtonC.isEnabled = false
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

                try {

                    myAnswer = radioGroup.findViewById<RadioButton>(checkedId).text.toString().toInt()

                }catch (e: Exception){
                    myAnswer = 0
                }

            }

        })

        btnAnswer!!.setOnClickListener {

            if (!init) {
                btnAnswer!!.text = "CONFIRMAR"
                generateQuestion()
                startTimer(30000)
                init = true
                radioButtonA.isEnabled = true
                radioButtonB.isEnabled = true
                radioButtonC.isEnabled = true

            }else{
                verifyAnswer()
            }


        }

    }

    private fun startTimer(timer_milli:Long) {

        if (timer!=null){
            timer?.cancel()
            timer = null
        }

        timer = object :CountDownTimer(timer_milli,1000){

            override fun onFinish() {

                for (p in listAnswers){
                    Log.i("LOG",p.toString())
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                timerNow = millisUntilFinished
                timerToEnd.text = (millisUntilFinished/1000).toInt().toString()
            }

        }


        timer?.start()
    }

    private fun verifyAnswer() {

        if (myAnswer == answer){

            listAnswers.add(Resposta(value1!!,value2!!, answer!!,op,true))

            Toast.makeText(activity,"Acertou", Toast.LENGTH_SHORT).show()

            generateQuestion()
            startTimer((timerNow!! + 3000))

        }else{

            listAnswers.add(Resposta(value1!!,value2!!, answer!!,op,false))

            if (timerNow!! > 3000) {
                generateQuestion()
                startTimer((timerNow!! - 3000))
            }else{
                timerToEnd.text = "0"
                Toast.makeText(activity,"Perdeu", Toast.LENGTH_SHORT).show()
                timer?.cancel()
            }

        }

        radioGroup.clearCheck()
    }

    private fun generateQuestion() {

        value1 = radomNumberGenerator(DIFICULTY)
        number1.text = value1.toString()
        value2 = radomNumberGenerator(DIFICULTY)
        number2.text = value2.toString()

        when(op){
            "+" -> answer = value1!! + value2!!
            "-" -> answer = value1!! - value2!!
            "*" -> answer = value1!! * value2!!
            "/" -> answer = value1!! / value2!!
        }

        val position = radomNumberGenerator(2)

        (radioGroup.getChildAt(0) as RadioButton).text = radomNumberGenerator(DIFICULTY).toString()
        (radioGroup.getChildAt(1) as RadioButton).text = radomNumberGenerator(DIFICULTY).toString()
        (radioGroup.getChildAt(2) as RadioButton).text = radomNumberGenerator(DIFICULTY).toString()

        (radioGroup.getChildAt(position) as RadioButton).text = answer.toString()

    }

    fun radomNumberGenerator(dificulty:Int): Int{
        return randomNumber.nextInt(dificulty)
    }

    override fun onResume() {
        super.onResume()
        init = false
        radioButtonA.isEnabled = false
        radioButtonB.isEnabled = false
        radioButtonC.isEnabled = false
    }


}