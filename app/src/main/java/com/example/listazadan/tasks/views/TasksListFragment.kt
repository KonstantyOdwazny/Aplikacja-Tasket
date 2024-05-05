package com.example.listazadan.tasks.views

import android.content.res.Resources
import com.example.listazadan.tasks.adapters.TaskAdapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Group
import com.example.listazadan.data.models.Task
import com.example.listazadan.databinding.FragmentTasksListBinding
import com.example.listazadan.tasks.viewmodel.TaskViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.example.listazadan.data.models.TaskFilter
import com.example.listazadan.tasks.viewmodel.GroupViewModel
import com.example.listazadan.tasks.viewmodel.GroupViewModelFactory


class TasksListFragment : Fragment() {

    private var _binding: FragmentTasksListBinding? = null
    private val binding get() = _binding!!


    private lateinit var taskViewModel: TaskViewModel
    private lateinit var groupViewModel: GroupViewModel

    private var groupArgument: Int = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTasksListBinding.inflate(inflater, container, false)

        val factory = TaskViewModelFactory((requireActivity().application as MyApp).taskRepository)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
        val groupFactory = GroupViewModelFactory((requireActivity().application as MyApp).groupRepository)
        groupViewModel = ViewModelProvider(this, groupFactory)[GroupViewModel::class.java]

        return binding.root
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

        groupArgument = arguments?.getInt("choosenGroup") ?: 1
        if (groupArgument != 1){
            taskViewModel.onGroupSelected(groupArgument)
        }

        val spinnerAdapter = ArrayAdapter<Group>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.groupSpinner.adapter = spinnerAdapter
        groupViewModel.groups.observe(viewLifecycleOwner, Observer { groupList ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(groupList)
            spinnerAdapter.notifyDataSetChanged()

            val defaultIndex = groupList.indexOfFirst { it.groupId == groupArgument }
            if (defaultIndex > 0) {
                binding.groupSpinner.setSelection(defaultIndex)
            }
        })

        val adapter = TaskAdapter(
            onTaskClick = { task ->
                // Otwórz fragment lub aktywność do edycji zadania
                val bundle = Bundle()
                bundle.putInt("taskID", task.id)
                bundle.putInt("groupID", task.groupId)
                findNavController().navigate(R.id.action_tasksListFragment_to_addTaskFragment, bundle)
            },
            onTaskDeleteClick = { task ->
                // Usuń zadanie
                taskViewModel.deleteTask(task)
            },
            onCheckClick = {task ->
                // Zmodyfikuj czy zadanie wykonane
                val newtask = Task(id = task.id,
                    description = task.description,
                    title = task.title,
                    date = task.date,
                    isCompleted = !task.isCompleted,
                    groupId = task.groupId)
                taskViewModel.updateTask(newtask)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val tabLayout = binding.tabs

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                taskViewModel.onTabSelected(tab?.position?.let { TaskFilter.fromInt(it) } ?: TaskFilter.ALL)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Opcjonalnie, kod jeśli potrzebny przy zmianie zakładki
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Opcjonalnie, kod jeśli potrzebny przy ponownym wybraniu zakładki
            }
        })

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3)) // All Tasks
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1)) // Todo
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2)) // Done

        binding.tabs.getTabAt(taskViewModel.selectedTab.index)?.select()

        taskViewModel.filteredTasks.observe(viewLifecycleOwner, Observer { tasks ->
            adapter.updateTasks(tasks)
        })

        binding.fab.setOnClickListener {

            val groupchoice: Int = (binding.groupSpinner.selectedItem as Group).groupId
            val bundle = Bundle()
            bundle.putInt("groupID", groupchoice)
            findNavController().navigate(R.id.action_tasksListFragment_to_addTaskFragment, bundle)
        }

        binding.groupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGroup = parent.getItemAtPosition(position) as Group
                // Filtruj dane na podstawie wybranej grupy
                taskViewModel.onGroupSelected(selectedGroup.groupId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Opcjonalnie, obsługa sytuacji, gdy nic nie jest wybrane
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

