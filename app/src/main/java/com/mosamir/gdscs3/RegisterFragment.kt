package com.mosamir.gdscs3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mosamir.gdscs3.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val userName = binding.etUserName.text.toString()
            val email = binding.etUserEmail.text.toString()
            val password = binding.etUserPassword.text.toString()
            val age = binding.etUserAge.text.toString()

            val user = User(userName = userName, email = email, password = password, age = age)

            lifecycleScope.launch(Dispatchers.IO) {

                if (email.isNotEmpty() && userDao.getUserByEmail(email) != null){
                    withContext(Dispatchers.Main){
                        binding.etUserEmail.error = "Email already exists"
                    }
                    return@launch
                }else if(userName.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty()){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "enter valid information", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                userDao.register(user)

                withContext(Dispatchers.Main){
                    val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(user)
                    findNavController().navigate(action)
                }
            }

        }

        binding.tvHaveAccount.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}