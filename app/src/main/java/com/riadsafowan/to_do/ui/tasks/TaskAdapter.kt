package com.riadsafowan.to_do.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.riadsafowan.to_do.data.local.room.task.Task
import com.riadsafowan.to_do.databinding.ItemTasksBinding

class TaskAdapter(private val listener: OnItemClickedListener) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskItemDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TaskViewHolder(private val binding: ItemTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener{
                    val position= adapterPosition
                    if (position!= RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemClicked(task)
                    }
                }
                checkboxCompleted.setOnClickListener {
                    val position= adapterPosition
                    if (position!= RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBoxClicked(task, checkboxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkboxCompleted.isChecked = task.isCompleted
                textViewName.text = task.taskName
                textViewName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant
            }
        }

    }

    interface OnItemClickedListener {
        fun onItemClicked(task: Task)
        fun onCheckBoxClicked(task: Task, isChecked: Boolean)
    }

    class TaskItemDiffUtilCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }

}