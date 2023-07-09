package com.mintusharma.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mintusharma.todoapp.models.TodoItem
import com.mintusharma.todoapp.databinding.RowItemBinding
import java.util.*

class TodoAdapter(private val todoItems: ArrayList<TodoItem>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onUpdateItemClick(position: Int)
        fun onDeleteItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = RowItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = todoItems[position]

        holder.binding.tittle.text=todoItem.tittle
        holder.binding.description.text=todoItem.description

        holder.binding.edit.setOnClickListener {
            mListener.onUpdateItemClick(position)
        }
        holder.binding.delete.setOnClickListener {
            mListener.onDeleteItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    fun setItems(items: List<TodoItem>) {
        todoItems.clear()
        todoItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}