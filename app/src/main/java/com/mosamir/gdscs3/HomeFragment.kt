package com.mosamir.gdscs3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mosamir.gdscs3.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentQuestionIndex = 0
    val args by navArgs<HomeFragmentArgs>()
    var question = false

    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = args.user

        val questions = listOf(
            Questions(question = "Do you have any information about the technical field?", answer = listOf("Yes", "No")),
            Questions(question = "Do you have an interest in a specific field?", answer = listOf("Yes", "No")),
            Questions(question = "What is the skill from below that you may have it ?", answer = listOf("Easy learner for statistics ", "Good designer eyes", "specificity specialist")),
        )

        displayQuestion(questions[currentQuestionIndex])

        binding.btnNext.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                displayQuestion(questions[currentQuestionIndex])
            } else {
                // Handle end of questions here
                val action = HomeFragmentDirections.actionHomeFragmentToCoursesFragment(user)
                findNavController().navigate(action)
            }
            if (question){
                user.questionnaire += 1
                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.updateUser(user)
                }
            }else{
                val action = HomeFragmentDirections.actionHomeFragmentToCoursesFragment(user)
                findNavController().navigate(action)
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            val selectedAnswer = radioButton.text.toString()
            // Handle the selected answer here
            question = if ((currentQuestionIndex == 0 || currentQuestionIndex == 1 ) && selectedAnswer == "Yes") {
                true
            } else currentQuestionIndex == 2
        }

    }

    private fun displayQuestion(question: Questions) {
        binding.tvQuestion.text = question.question
        binding.radioGroup.removeAllViews()
        for (answer in question.answer) {
            val radioButton = RadioButton(requireContext())
            radioButton.text = answer
            binding.radioGroup.addView(radioButton)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}