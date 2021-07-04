package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.rsschool.quiz.databinding.ActivityMainBinding
import com.rsschool.quiz.databinding.FragmentQuizBinding

class MainActivity : AppCompatActivity(), ChangePage, ResultFragment.Restart {

    private lateinit var binding: ActivityMainBinding

    private var correctAnswers = arrayOf(-1, 3, 1, 1, 4, 3)
    private var question = 1
    private var chosenAnswers = IntArray(6) { -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun nextPage(item: Int) {
        chosenAnswers[question] = item
        question++
        changePage()
        changeThem()
    }

    override fun previousPage(item: Int) {
        chosenAnswers[question] = item
        question--
        if (question < 1) {
            question = 1
        } else {
            changePage()
            changeThem()
        }
    }

    override fun restart() {
        chosenAnswers = IntArray(6) { -1 }
        question = 1
        changePage()
        changeThem()
    }

    private fun getResult(): String {
        var result = 0
        for (i in 1 until chosenAnswers.size) {
            if (chosenAnswers[i] == correctAnswers[i]) result++
        }
        return "Your result: $result/${correctAnswers.size - 1}"
    }

    private fun changePage() {
        if (question == 6) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainerView, ResultFragment
                        .newInstance(getResult(), chosenAnswers)
                ).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainerView, QuizFragment
                        .newInstance(chosenAnswers[question], question)
                ).commit()
        }
    }

    private fun changeThem() {
       val them = when (question) {
           1 -> {
               window.statusBarColor = ContextCompat.getColor(this, R.color.deep_orange_100_dark)
               R.style.Theme_Quiz_First
           }
           2 -> {
               window.statusBarColor = ContextCompat.getColor(this, R.color.yellow_100_dark)
               R.style.Theme_Quiz_Second}
           3 -> {
               window.statusBarColor = ContextCompat.getColor(this, R.color.light_green_100_dark)
               R.style.Theme_Quiz_Third
           }
           4 -> {
               window.statusBarColor = ContextCompat.getColor(this, R.color.deep_purple_100_dark)
               R.style.Theme_Quiz_Fourth
           }
           5 -> {
               window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_100_dark)
               R.style.Theme_Quiz_Fifth
           }
           else -> R.style.Theme_Quiz_First
       }
        theme.applyStyle(them, true)
    }
}
