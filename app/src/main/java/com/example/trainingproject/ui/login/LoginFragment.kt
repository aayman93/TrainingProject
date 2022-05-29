package com.example.trainingproject.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trainingproject.databinding.FragmentLoginBinding
import com.example.trainingproject.util.EventObserver
import com.example.trainingproject.util.snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

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
        init()
        setListeners()
        subscribeToObservables()
    }

    private fun init() {
        if (isUserLogged()) navigateToHome()
    }

    private fun setListeners() {
        with(binding) {
            buttonLogin.setOnClickListener {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()
                viewModel.login(email, password)
            }

            tvRegister.setOnClickListener {
                navigateToRegister()
            }
        }
    }

    private fun subscribeToObservables() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = { errorRes, message ->
                binding.progressBar.isVisible = false
                binding.buttonLogin.isVisible = true
                if (errorRes == null) snackbar(message) else snackbar(errorRes)
            },
            onLoading = {
                binding.progressBar.isVisible = true
                binding.buttonLogin.isVisible = false
            }
        ) {
            binding.progressBar.isVisible = false
            binding.buttonLogin.isVisible = true
            navigateToHome()
        })
    }

    private fun isUserLogged(): Boolean {
        return auth.currentUser != null
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