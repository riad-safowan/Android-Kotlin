package com.riadsafowan.to_do.ui.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.databinding.ItemPostsBinding
import com.riadsafowan.to_do.databinding.ItemTasksBinding

class PostAdapter(private val listener: OnItemClickedListener) :
    ListAdapter<PostResponse, PostAdapter.PostViewHolder>(PostItemDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PostViewHolder(private val binding: ItemPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.apply {
//                root.setOnClickListener{
//                    val position= adapterPosition
//                    if (position!= RecyclerView.NO_POSITION){
//                        val task = getItem(position)
//                        listener.onItemClicked(task)
//                    }
//                }
//                checkboxCompleted.setOnClickListener {
//                    val position= adapterPosition
//                    if (position!= RecyclerView.NO_POSITION){
//                        val task = getItem(position)
//                        listener.onCheckBoxClicked(task, checkboxCompleted.isChecked)
//                    }
//                }

            }
        }

        fun bind(post: PostResponse) {
            binding.apply {
//                checkboxCompleted.isChecked = task.isCompleted
//                textViewName.text = task.taskName
//                textViewName.paint.isStrikeThruText = task.isCompleted
//                labelPriority.isVisible = task.isImportant
                Glide.with(binding.root)
                    .load(post.userImageUrl)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(binding.image)
                name.text = post.userName
                text.text = post.text
            }
        }

    }

    interface OnItemClickedListener {
        fun onItemClicked(post: PostResponse)
        fun onCheckBoxClicked(post: PostResponse, isChecked: Boolean)
    }

    class PostItemDiffUtilCallback : DiffUtil.ItemCallback<PostResponse>() {
        override fun areItemsTheSame(oldItem: PostResponse, newItem: PostResponse) =
            oldItem.postId == newItem.postId

        override fun areContentsTheSame(oldItem: PostResponse, newItem: PostResponse) =
            oldItem == newItem
    }

}