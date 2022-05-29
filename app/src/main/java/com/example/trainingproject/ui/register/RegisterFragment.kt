package com.example.trainingproject.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trainingproject.databinding.FragmentRegisterBinding
import com.example.trainingproject.util.EventObserver
import com.example.trainingproject.util.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

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
        setListeners()
        subscribeToObservables()
    }

    private fun setListeners() {
        with(binding) {
            buttonRegister.setOnClickListener {
                val username = inputUsername.text.toString()
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()
                val confirmedPassword = inputConfirmPassword.text.toString()

                viewModel.register(username, email, password, confirmedPassword)
            }
        }
    }

    private fun subscribeToObservables() {
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = { errorRes, message ->
                binding.progressBar.isVisible = false
                binding.buttonRegister.isVisible = true
                if (errorRes == null) snackbar(message) else snackbar(errorRes)
            },
            onLoading = {
                binding.progressBar.isVisible = true
                binding.buttonRegister.isVisible = false
            }
        ) {
            binding.progressBar.isVisible = false
            binding.buttonRegister.isVisible = true
            navigateToHome()
        })
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