package com.rsschool.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.rsschool.quiz.databinding.FragmentQuizBinding



class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var changePage: ChangePage? = null
    private var array: Array<String?>? = null


    private var choice: Int = -1
    private var number: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            choice = it.getInt(ARG_CHOICE)
            number = it.getInt(ARG_NUMBER_OF_QUESTION)
        }
        changePage = context as ChangePage
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater)
        binding.toolbar.title = "Question $number"
        val questionArray = when (number) {
            2 -> R.array.question_2
            3 -> R.array.question_3
            4 -> R.array.question_4
            5 -> R.array.question_5
            else -> R.array.question_1
        }
        array = resources.getStringArray(questionArray)
        array?.let {
            with(binding) {
                question.text = it[0]
                optionOne.text = it[1]
                optionTwo.text = it[2]
                optionThree.text = it[3]
                optionFour.text = it[4]
                optionFive.text = it[5]
            }
        }

        return binding.root
    }

    private fun getItem(): Int {
        val check = binding.radioGroup.checkedRadioButtonId
        val item = binding.radioGroup.indexOfChild(activity?.findViewById(check))
        return item
    }

    private fun setItem() {
        if (choice in 0..4) {
            binding.radioGroup.check(binding.radioGroup.getChildAt(choice).id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setItem()
        if (binding.radioGroup.checkedRadioButtonId < 0) {
            binding.nextButton.isVisible = false
        } else {
            binding.nextButton.setOnClickListener {
                changePage?.nextPage(getItem())
            }
        }
        if (number == 1) {
            binding.toolbar.navigationIcon = ResourcesCompat
                .getDrawable(resources, android.R.drawable.menuitem_background, null)
            binding.previousButton.isVisible = false
        } else {
            binding.previousButton.setOnClickListener {
                changePage?.previousPage(getItem())
            }
            binding.toolbar.setNavigationOnClickListener {
                changePage?.previousPage(getItem())
            }
        }
        if (number == 5) {
            binding.nextButton.text = getString(R.string.button_submit)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            binding.nextButton.isVisible = true
            binding.nextButton.setOnClickListener {
                changePage?.nextPage(getItem())
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(choice: Int, number: Int) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CHOICE, choice)
                    putInt(ARG_NUMBER_OF_QUESTION, number)
                }
            }
        private const val ARG_CHOICE = "choice"
        private const val ARG_NUMBER_OF_QUESTION = "numberOfQuestion"
    }
}