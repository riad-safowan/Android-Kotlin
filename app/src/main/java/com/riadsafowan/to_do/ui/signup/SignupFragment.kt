package com.riadsafowan.to_do.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.model.signup.SignupRequest
import com.riadsafowan.to_do.databinding.FragmentSignupBinding
import com.riadsafowan.to_do.ui.login.ui.login.LoggedInUserView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private var _binding: FragmentSignupBinding? = null
    private val viewModel: SignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignupBinding.bind(view)

        viewModel.signupFormState.observe(viewLifecycleOwner) { signupFormState ->
            if (signupFormState == null) {
                return@observe
            }
            binding.login.isEnabled = signupFormState.isDataValid
            signupFormState.firstnameError?.let {
                if (binding.firstName.text.toString().isNotEmpty())
                    binding.firstName.error = getString(it)
            }
            signupFormState.lastnameError?.let {
                if (binding.lastName.text.toString().isNotEmpty())
                    binding.lastName.error = getString(it)
            }
            signupFormState.phnError?.let {
                if (binding.phone.text.toString().isNotEmpty())
                    binding.phone.error = getString(it)
            }
            signupFormState.emailError?.let {
                if (binding.email.text.toString().isNotEmpty())
                    binding.email.error = getString(it)
            }
            signupFormState.passwordError?.let {
                if (binding.password.text.toString().isNotEmpty())
                    binding.password.error = getString(it)
            }

        }

        viewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            loginResult ?: return@observe
            binding.loading.visibility = View.GONE
            loginResult.error?.let {
                showLoginFailed(it)
            }
            loginResult.success?.let {
                updateUiWithUser(it)
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.signupDataChanged(
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.phone.text.toString(),
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
            }
        }

        binding.firstName.addTextChangedListener(afterTextChangedListener)
        binding.password.addTextChangedListener(afterTextChangedListener)
        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.signup(
                    SignupRequest(
                        firstName = binding.firstName.text.toString(),
                        lastName = binding.lastName.text.toString(),
                        phoneNumber = binding.phone.text.toString(),
                        email = binding.email.text.toString(),
                        password = binding.password.text.toString()
                    )
                )
            }
            false
        }

        binding.login.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.signup(
                SignupRequest(
                    firstName = binding.firstName.text.toString(),
                    lastName = binding.lastName.text.toString(),
                    phoneNumber = binding.phone.text.toString(),
                    email = binding.email.text.toString(),
                    password = binding.password.text.toString()
                )
            )
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + " " + model.displayName
        findNavController().navigate(R.id.tasksFragment)
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}