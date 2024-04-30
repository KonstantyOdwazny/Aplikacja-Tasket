package com.example.listazadan.tasks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listazadan.R
import com.example.listazadan.data.models.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    private var _tasks: List<Task> = emptyList()

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskTitle: TextView = view.findViewById(R.id.taskTitle)
        val taskCheckbox: CheckBox = view.findViewById(R.id.taskCheckbox)
        val deleteButton: FloatingActionButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = _tasks[position]
        holder.taskTitle.text = task.title
        holder.taskCheckbox.isChecked = task.isCompleted

        // Listener dla tytułu zadania
        holder.taskTitle.setOnClickListener {
            onTaskClick(task)
        }

        // Listener dla przycisku usuwania
        holder.deleteButton.setOnClickListener {
            onTaskDeleteClick(task)
        }
    }

    override fun getItemCount() : Int = _tasks.size

    fun updateTasks(tasks: List<Task>) {
        this._tasks = tasks
        notifyDataSetChanged()
    }
}
