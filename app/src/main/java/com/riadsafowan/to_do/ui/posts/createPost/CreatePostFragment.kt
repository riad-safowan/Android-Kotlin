package com.riadsafowan.to_do.ui.posts.createPost

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.databinding.FragmentCreatePostBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private lateinit var binding: FragmentCreatePostBinding
    private val viewModel: CreatePostViewModel by viewModels()
    private var imageURI: Uri? = null

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
            if (binding.textBox.text.toString().trim().isNotEmpty() || imageURI!=null)
            viewModel.createPost(binding.textBox.text.toString())
            else
                Toast.makeText(requireContext(), "Please add text or image", Toast.LENGTH_SHORT).show()
        }

        binding.addImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .maxResultSize(1000, 1000)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        viewModel.PostResponse.observe(viewLifecycleOwner) {
            if (imageURI != null) {
                val imageFile = File(imageURI!!.path)

                viewModel.uploadImage(
                    MultipartBody.Part.createFormData(
                        "image", imageFile.getName(), imageFile
                            .asRequestBody("image/*".toMediaTypeOrNull())
                    ), it.postId!!
                )
            } else {
                findNavController().navigate(R.id.postsFragment)
            }
        }
        viewModel.ImageResponse.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.postsFragment)
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                imageURI = data?.data!!
                Glide.with(this)
                    .load(imageURI)
                    .into(binding.imageView)
                binding.imageView.visibility = View.VISIBLE
            }
        }
}