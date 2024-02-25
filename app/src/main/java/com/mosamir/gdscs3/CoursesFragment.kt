package com.mosamir.gdscs3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosamir.gdscs3.databinding.FragmentCoursesBinding
import com.mosamir.gdscs3.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    val args by navArgs<CoursesFragmentArgs>()
    private lateinit var coursesAdapter: CoursesAdapter

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
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = args.user
        Log.e("TAG", "onViewCreated: $user", )

        val courses = listOf(
            Course(id = 1,title = "Android Development", image = R.drawable.android),
            Course(id = 2,title = "Node JS",image = R.drawable.web),
            Course(id = 3,title = "Machine Learning", image = R.drawable.ml),
            Course(id = 4,title = "Data Analysis", image = R.drawable.da),
            Course(id = 5,title = "Laravel", image = R.drawable.la),
            Course(id = 6,title = "flutter", image = R.drawable.fl),
            Course(id = 7,title = "React", image = R.drawable.rjs),
            Course(id = 8,title = "HTML", image = R.drawable.html),
        )
        coursesAdapter = CoursesAdapter()
        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = coursesAdapter
        }
        if (user.questionnaire == 1) {
            val filteredCourses = courses.filter { it.id == 1 || it.id == 7 || it.id == 8 || it.id == 6}
            coursesAdapter.submitList(filteredCourses)
        } else if (user.questionnaire == 2) {
            val filteredCourses = courses.filter { it.id == 2 || it.id == 3 || it.id == 4 || it.id == 5}
            coursesAdapter.submitList(filteredCourses)
        } else if (user.questionnaire == 3) {
            coursesAdapter.submitList(courses)
        }

        binding.ivLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                userDao.updateUser(user.copy(questionnaire = 0))
            }
            val action = CoursesFragmentDirections.actionCoursesFragmentToLoginFragment()
            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}