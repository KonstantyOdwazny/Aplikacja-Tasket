package com.example.listazadan.tasks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listazadan.R
import com.example.listazadan.data.models.Group
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupAdapter(
    private val onGroupEditClick: (Group) -> Unit,
    private val onGroupDeleteClick: (Group) -> Unit,
    private val onGroupClick: (Group) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private var _groups: List<Group> = emptyList()

    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupTitle: TextView = view.findViewById(R.id.groupName)
        val deleteButton: FloatingActionButton = view.findViewById(R.id.group_btn_delete)
        val editButton: FloatingActionButton = view.findViewById(R.id.fb_edit_group)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int = _groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = _groups[position]

        holder.groupTitle.text = group.name

        holder.groupTitle.setOnClickListener {
            onGroupClick(group)
        }

        holder.deleteButton.setOnClickListener{
            onGroupDeleteClick(group)
        }

        holder.editButton.setOnClickListener {
            onGroupEditClick(group)
        }

    }


    fun updateGroups(groups: List<Group>){
        this._groups = groups
        notifyDataSetChanged()
    }
}