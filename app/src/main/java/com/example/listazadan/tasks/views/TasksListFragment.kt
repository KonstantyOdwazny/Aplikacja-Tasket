package com.example.listazadan.tasks.views

import android.content.res.Resources
import com.example.listazadan.tasks.adapters.TaskAdapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Task
import com.example.listazadan.tasks.viewmodel.TaskViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class TasksListFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Dodanie paddingow

        try {
            val topPadding = resources.getDimensionPixelSize(R.dimen.toolbar_height) // Definiujesz w dimens.xml
            val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height) // Definiujesz w dimens.xml
            view.setPadding(0, topPadding, 0, bottomPadding)
        }catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Resource not found: " + e.message)
        }

        val factory = TaskViewModelFactory((requireActivity().application as MyApp).taskRepository)
        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = TaskAdapter(
            onTaskClick = { task ->
                // Otwórz fragment lub aktywność do edycji zadania
                val bundle = Bundle()
                bundle.putInt("taskID", task.id)
                findNavController().navigate(R.id.action_tasksListFragment_to_addTaskFragment, bundle)
            },
            onTaskDeleteClick = { task ->
                // Usuń zadanie
                viewModel.deleteTask(task)
            },
            onCheckClick = {task ->
                // Zmodyfikuj czy zadanie wykonane
                val newtask = Task(id = task.id,
                    description = task.description,
                    title = task.title,
                    date = task.date,
                    isCompleted = !task.isCompleted)
                viewModel.updateTask(newtask)
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val allTabs = view.findViewById<TabLayout>(R.id.tabs)
        allTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.onTabSelected(tab?.position ?: 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Opcjonalnie, kod jeśli potrzebny przy zmianie zakładki
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Opcjonalnie, kod jeśli potrzebny przy ponownym wybraniu zakładki
            }
        })

        allTabs.getTabAt(viewModel.selectedTab)?.select()

        viewModel.filteredTasks.observe(viewLifecycleOwner, Observer { tasks ->
            adapter.updateTasks(tasks)
        })
//        viewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
//            adapter.updateTasks(tasks)
//        })

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            // Użyj NavController do nawigacji do fragmentu dodawania zadania
            findNavController().navigate(R.id.action_tasksListFragment_to_addTaskFragment)
        }

    }
}

