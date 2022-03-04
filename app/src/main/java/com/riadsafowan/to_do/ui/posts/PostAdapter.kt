package com.riadsafowan.to_do.ui.posts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.databinding.ItemPostsBinding

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
                likeBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val post = getItem(position)
                        post.isliked = !post.isliked!!
                        post.likes = if (post.isliked!!) {
                            post.likes!! + 1

                        } else {
                            post.likes!! - 1
                        }
                        bind(post)
                        listener.onLikeBtnClicked(post.postId!!)
                    }
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(post: PostResponse) {
            binding.apply {
                Glide.with(binding.root)
                    .load(post.userImageUrl)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(binding.image)
                name.text = post.userName
                text.text = post.text
                if (post.isliked!!)
                    likeBtn.setColorFilter(ContextCompat.getColor(likeBtn.context, R.color.black))
                else likeBtn.setColorFilter(ContextCompat.getColor(likeBtn.context, R.color.normal))
                likes.text = post.likes.toString() + if (post.likes!! > 1) " likes" else " like"
                comments.text =
                    post.comments.toString() + if (post.comments!! > 1) " comments" else " comment"
            }
        }

    }

    interface OnItemClickedListener {
        fun onItemClicked(post: PostResponse)
        fun onLikeBtnClicked(postId: Int)
    }

    class PostItemDiffUtilCallback : DiffUtil.ItemCallback<PostResponse>() {
        override fun areItemsTheSame(oldItem: PostResponse, newItem: PostResponse) =
            oldItem.postId == newItem.postId

        override fun areContentsTheSame(oldItem: PostResponse, newItem: PostResponse) =
            oldItem == newItem
    }

}