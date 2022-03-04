package com.riadsafowan.to_do.ui.posts

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.local.room.task.Task
import com.riadsafowan.to_do.data.local.pref.SortOrder
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.databinding.FragmentPostsBinding
import com.riadsafowan.to_do.databinding.FragmentTasksBinding
import com.riadsafowan.to_do.ui.main.MainViewModel
import com.riadsafowan.to_do.ui.tasks.TaskAdapter
import com.riadsafowan.to_do.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.*

@AndroidEntryPoint
class PostsFragment : Fragment(R.layout.fragment_posts), PostAdapter.OnItemClickedListener {
    private lateinit var binding: FragmentPostsBinding
    private val viewModel: PostsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postAdapter = PostAdapter(this)
        binding.apply {
            recyclerViewPosts.apply {
                adapter = postAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewModel.posts.observe(viewLifecycleOwner) {
                postAdapter.submitList(it)
            }

            fabAddPosts.setOnClickListener {
                findNavController().navigate(R.id.createPostFragment)
            }
        }

    }

    override fun onItemClicked(post: PostResponse) {

    }

    override fun onLikeBtnClicked(postId: Int) {
        viewModel.likePost(postId)

    }
}