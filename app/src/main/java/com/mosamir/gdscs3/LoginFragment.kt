package com.mosamir.gdscs3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mosamir.gdscs3.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etUserEmail.text.toString()
            val password = binding.etUserPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                // check if email and password are correct
                if (email.isNotEmpty() && password.isNotEmpty()){
                    // navigate to home fragment
                    val user = userDao.login(email, password)
                    if (user != null){
                        withContext(Dispatchers.Main){
                            if (user.questionnaire > 0){
                                val action = LoginFragmentDirections.actionLoginFragmentToCoursesFragment(user)
                                findNavController().navigate(action)
                            }else{
                                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(user)
                                findNavController().navigate(action)
                            }
                        }
                    }else{
                        // show error message
                        withContext(Dispatchers.Main){
                            binding.etUserEmail.error = "Email or password is incorrect"
                        }
                    }
                }else{
                    withContext(Dispatchers.Main){
                        binding.etUserEmail.error = "Email or password is incorrect"
                    }
                }
            }
        }

        binding.tvCreateNewAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}