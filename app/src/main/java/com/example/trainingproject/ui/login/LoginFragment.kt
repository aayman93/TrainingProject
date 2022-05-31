package com.example.trainingproject.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainingproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToHomeIfLogged()

        binding.buttonLogin.setOnClickListener {
            login()
        }

        binding.tvRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun login() {
        binding.buttonLogin.isVisible = false
        binding.progressBar.isVisible = true

        Firebase.auth.signInWithEmailAndPassword(
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString()
        ).addOnSuccessListener {
            binding.progressBar.isVisible = false
            navigateToHome()
        }.addOnFailureListener {
            binding.progressBar.isVisible = false
            binding.buttonLogin.isVisible = true
            val message = it.message ?: "Unexpected error occurred!"
            Firebase.crashlytics.log(message)
            Log.e("Firebase", message, it)
        }
    }

    private fun navigateToHomeIfLogged() {
        if (Firebase.auth.currentUser != null) navigateToHome()
    }

    private fun navigateToHome() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun navigateToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}