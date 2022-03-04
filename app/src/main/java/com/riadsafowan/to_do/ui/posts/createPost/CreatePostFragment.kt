package com.riadsafowan.to_do.ui.posts.createPost

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.riadsafowan.to_do.databinding.FragmentCreatePostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private lateinit var binding: FragmentCreatePostBinding
    private val viewModel: CreatePostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val binding = FragmentCreatePostBinding.bind(view)

        binding.btnPost.setOnClickListener {
            viewModel.createPost(binding.textBox.text.toString())
        }

        viewModel.PostResponse.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.text, Toast.LENGTH_SHORT).show()
        }


    }
}