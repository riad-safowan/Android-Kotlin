package com.riadsafowan.to_do.ui.posts.comments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.data.model.posts.comment.CommentResponse
import com.riadsafowan.to_do.databinding.ItemCommentBinding

class CommentsAdapter(private val listener: OnItemClickedListener) :
    ListAdapter<CommentResponse, CommentsAdapter.CommentsViewHolder>(CommentItemDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CommentsViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {


//        init {
//            binding.apply {
//                root.setOnClickListener {
//                    val position = adapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        val task = getItem(position)
//                        listener.onItemClicked(task)
//                    }
//                }
//                likeBtn.setOnClickListener {
//                    val position = adapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        val post = getItem(position)
//                        post.isliked = !post.isliked!!
//                        post.likes = if (post.isliked!!) {
//                            post.likes!! + 1
//
//                        } else {
//                            post.likes!! - 1
//                        }
//                        bind(post)
//                        listener.onLikeBtnClicked(post.postId!!)
//                    }
//                }
//            }
//        }

        @SuppressLint("SetTextI18n")
        fun bind(comment: CommentResponse) {
            binding.apply {
                Glide.with(binding.root)
                    .load(comment.userImgUrl)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(binding.image)
                name.text = comment.userName
                if (comment.text.isNullOrEmpty()) {
                    text.visibility = View.GONE
                } else {
                    text.text = comment.text
                    text.visibility = View.VISIBLE
                }
            }
        }

    }

    interface OnItemClickedListener {
        fun onItemClicked(comment: CommentResponse)
        fun onLikeBtnClicked(postId: Int)
    }

    class CommentItemDiffUtilCallback : DiffUtil.ItemCallback<CommentResponse>() {
        override fun areItemsTheSame(oldItem: CommentResponse, newItem: CommentResponse) =
            oldItem.postId == newItem.postId

        override fun areContentsTheSame(oldItem: CommentResponse, newItem: CommentResponse) =
            oldItem == newItem
    }

}