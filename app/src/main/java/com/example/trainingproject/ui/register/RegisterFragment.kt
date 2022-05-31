package com.example.trainingproject.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainingproject.databinding.FragmentRegisterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        binding.buttonRegister.isVisible = false
        binding.progressBar.isVisible = true

        Firebase.auth.createUserWithEmailAndPassword(
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString()
        ).addOnSuccessListener {
            binding.progressBar.isVisible = false
            navigateToHome()
        }.addOnFailureListener {
            binding.progressBar.isVisible = false
            binding.buttonRegister.isVisible = true
            val message = it.message ?: "Unexpected error occurred!"
            Firebase.crashlytics.log(message)
            Log.e("Firebase", message, it)
        }
    }

    private fun navigateToHome() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}