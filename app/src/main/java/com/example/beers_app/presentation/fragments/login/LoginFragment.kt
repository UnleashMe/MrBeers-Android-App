package com.example.beers_app.presentation.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beers.R
import com.example.beers.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

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

        addTextChangedListeners()
        observeErrorsAndEnableButton()
        setupButtons()
        showToastOnEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTextChangedListeners() {
        binding.editTextPhone.addTextChangedListener {
            viewModel.onNumberInput(it.toString())
        }
        binding.editTextPassword.addTextChangedListener {
            viewModel.onPasswordInput(it.toString())
        }
    }

    private fun observeErrorsAndEnableButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.textLayoutPhone.error = it.errors.numberError
                binding.textLayoutPassword.error = it.errors.passwordError
                binding.LoginButton.isEnabled = it.isButtonEnabled
            }
        }
    }

    private fun setupButtons() {
        binding.LoginButton.setOnClickListener {
            viewModel.login()
        }
        binding.CreateAccountButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionGreetingFragmentToRegisterFragment())
        }
    }

    private fun showToastOnEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginEvent.collectLatest {
                    Toast.makeText(requireContext(), getString(R.string.welcome_toast, it), Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListFragment())
                }
            }
        }
    }
}