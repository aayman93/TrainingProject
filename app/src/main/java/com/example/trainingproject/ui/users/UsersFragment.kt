package com.example.trainingproject.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trainingproject.databinding.FragmentUsersBinding
import com.example.trainingproject.util.EventObserver
import com.example.trainingproject.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UsersViewModel>()

    @Inject
    lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupUsersRecycler()
        subscribeToObservables()
    }

    private fun setListeners() {
        setOnBackButtonClickListener()
        setOnUserAdapterClickListener()
    }

    private fun setupUsersRecycler() {
        binding.usersRecycler.adapter = usersAdapter
    }

    private fun subscribeToObservables() {
        viewModel.users.observe(viewLifecycleOwner, EventObserver(
            onError = { errorRes, message ->
                binding.progressBar.isVisible = false
                binding.usersRecycler.isVisible = false
                errorRes?.let { snackbar(it) } ?: snackbar(message)
            },
            onLoading = {
                binding.progressBar.isVisible = true
                binding.usersRecycler.isVisible = false
            }
        ) {
            binding.progressBar.isVisible = false
            binding.usersRecycler.isVisible = true

            usersAdapter.submitList(it)
        })
    }

    private fun setOnBackButtonClickListener() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setOnUserAdapterClickListener() {
        usersAdapter.setOnUserClickListener { user ->
            val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(user.uid)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}