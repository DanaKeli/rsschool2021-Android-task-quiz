package com.rsschool.quiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    interface Restart {
        fun restart()
    }

    private var result: String? = ""
    private var answersArray: IntArray? = null

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var restart: Restart? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getString(ARG_RESULT)
            answersArray = it.getIntArray(ARG_ANSWERS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewResult.text = result

        binding.imageViewRestart.setOnClickListener {
            restart = context as Restart
            restart?.restart()
        }

        binding.imageViewShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz result")
            intent.putExtra(Intent.EXTRA_TEXT, message())
            intent.setData(Uri.parse("mailto:"));
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Complete action using"))
        }

        binding.imageViewOff.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun message(): String {
        val builder: StringBuilder = java.lang.StringBuilder()
        var array: Array<String> = Array(5) { "" }
        builder.append(result).append("\n")
        for (i in 1..5) {
            when (i) {
                1 -> array = resources.getStringArray(R.array.question_1)
                2 -> array = resources.getStringArray(R.array.question_2)
                3 -> array = resources.getStringArray(R.array.question_3)
                4 -> array = resources.getStringArray(R.array.question_4)
                5 -> array = resources.getStringArray(R.array.question_5)
            }
            builder.append("$i. ").append(array[0]).append("\n")
            answersArray?.let {
                builder.append("Your answer: ").append(array[it[i] + 1]).append("\n\n")
            }
        }
        return builder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(result: String, answersArray: IntArray) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RESULT, result)
                    putIntArray(ARG_ANSWERS, answersArray)
                }
            }

        private const val ARG_RESULT = "result"
        private const val ARG_ANSWERS = "answers"
    }
}