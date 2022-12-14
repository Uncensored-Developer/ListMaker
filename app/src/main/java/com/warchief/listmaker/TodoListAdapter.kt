package com.warchief.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TodoListAdapter(private val lists: ArrayList<TaskList>, private val clickListener: TodoListClickListener): RecyclerView.Adapter<TodoListViewHolder>() {

    interface TodoListClickListener {
        fun listItemClicked(list: TaskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_view_holder, parent, false)
        return TodoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.listPositionTextView.text = (position + 1).toString()
        holder.listTitleTextView.text = lists[position].name
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun addList(list: TaskList) {
        lists.add(list)
        notifyItemInserted(lists.size - 1)
    }
}