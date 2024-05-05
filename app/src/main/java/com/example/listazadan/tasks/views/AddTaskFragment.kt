package com.example.listazadan.tasks.views

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Group
import com.example.listazadan.data.models.Task
import com.example.listazadan.databinding.FragmentAddTaskBinding
import com.example.listazadan.tasks.viewmodel.GroupViewModel
import com.example.listazadan.tasks.viewmodel.GroupViewModelFactory
import com.example.listazadan.tasks.viewmodel.TaskViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar
import androidx.fragment.app.FragmentActivity


class AddTaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var groupViewModel: GroupViewModel
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private var taskID: Int = -1

    private var selectedGroup: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        val groupFactory = GroupViewModelFactory((requireActivity().application as MyApp).groupRepository)
        groupViewModel = ViewModelProvider(this, groupFactory)[GroupViewModel::class.java]
        val taskFactory = TaskViewModelFactory((requireActivity().application as MyApp).taskRepository)
        taskViewModel = ViewModelProvider(this, taskFactory)[TaskViewModel::class.java]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Pokaż Toolbar i BottomNavigationView
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Padding na bottom navigation
        val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height) // Definiujesz w dimens.xml
        this.view?.setPadding(0, 0, 0, bottomPadding)

        // Ukrywanie toolbar i bottom navigation
        (activity as MainActivity).supportActionBar?.hide()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE

        // Sprawdzenie wchodzących argumentów
        taskID = arguments?.getInt("taskID") ?: -1

        selectedGroup = arguments?.getInt("groupID") ?: 1

        observeViewModel()
        setupSpinner()
        configureListeners()

    }

    fun showDatePickerDialog(context: Context, setDate: (year: Int, month: Int, dayOfMonth: Int) -> Unit) {
        // Uzyskanie bieżącej daty
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Tworzenie nowego DatePickerDialog
        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            // Ta funkcja zostanie wywołana, gdy data zostanie wybrana
            setDate(selectedYear, selectedMonth, selectedDay)
        }, year, month, day)

        // Ustawianie minimalnej daty
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000  // Zakaz wyboru daty w przeszłości

        // Ustawianie maksymalnej daty
        val maxCalendar = Calendar.getInstance()
        maxCalendar.set(Calendar.YEAR, 2030)
        datePickerDialog.datePicker.maxDate = maxCalendar.timeInMillis

        datePickerDialog.show()  // Pokaż dialog
    }
    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Błąd")
            .setMessage("Niemożliwe utworzenie zadania bez tytułu. Proszę uzupełnij pole.")
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter<Group>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGroup.adapter = adapter
        groupViewModel.groups.observe(viewLifecycleOwner, Observer { groupList ->
            adapter.clear()
            adapter.addAll(groupList)
            adapter.notifyDataSetChanged()

            val defaultIndex = groupList.indexOfFirst { it.groupId == selectedGroup }
            if (defaultIndex > 0) {
                binding.spinnerGroup.setSelection(defaultIndex)
            }
        })
    }

    private fun configureListeners(){

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog(requireContext()) { year, month, dayOfMonth ->
                val dateString = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.editTextDate.setText(dateString)
            }
        }

        binding.buttonSave.setOnClickListener {
            val title: String = binding.editTextTitle.text.toString()
            val description: String = binding.editTextDescription.text.toString()
            val datetext: String = binding.editTextDate.text.toString()
            val groupchoice: Int = (binding.spinnerGroup.selectedItem as Group).groupId

            if (title.isBlank()){
                showAlertDialog()
            }
            else {
                if (taskID != -1) {
                    val updatedtask = Task(
                        id = taskID,
                        title = title,
                        description = description,
                        date = datetext,
                        isCompleted = false,
                        groupId = groupchoice
                    )
                    taskViewModel.updateTask(updatedtask)
                } else {
                    val newtask = Task(
                        title = title,
                        description = description,
                        date = datetext,
                        isCompleted = false,
                        groupId = groupchoice
                    )
                    taskViewModel.addTask(newtask)
                }
                // Navigacja z powrotem do listy zadań lub poprzedniego ekranu
                findNavController().popBackStack()  // Adjust the ID
            }
        }

        binding.buttonCancel.setOnClickListener {
            // Navigacja z powrotem do listy zadań lub poprzedniego ekranu
            findNavController().popBackStack()
        }

    }

    private fun observeViewModel(){
        if (taskID != -1){
            taskViewModel.getTaskById(taskID).observe(viewLifecycleOwner) { task ->
                binding.editTextTitle.setText(task.title)
                binding.editTextDescription.setText(task.description)
                binding.editTextDate.setText(task.date)
                selectedGroup = task.groupId
            }
        }
    }

}