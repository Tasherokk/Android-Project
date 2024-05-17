package com.example.aniframe.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.aniframe.R
import com.example.aniframe.data.database.AuthManager
import com.example.aniframe.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        authManager = AuthManager(requireContext().applicationContext)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.loginButton.setOnClickListener {
            handleLogin()
        }
        binding.signUpText.setOnClickListener{
            replaceFragment(RegisterFragment())
        }
    }
    private fun handleLogin() {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
        } else if (authManager.validateUser(username, password)) {
            authManager.saveAuthToken("sample_token")
            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
            replaceFragment(FavoritesFragment())
        } else {
            Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }

}