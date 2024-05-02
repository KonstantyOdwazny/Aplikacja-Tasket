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
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Group
import com.example.listazadan.data.models.Task
import com.example.listazadan.databinding.FragmentAddTaskBinding
import com.example.listazadan.tasks.viewmodel.GroupViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar


class AddTaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        setupSpinner(binding.spinnerGroup, groupViewModel.groups)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Pokaż Toolbar i BottomNavigationView
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height) // Definiujesz w dimens.xml
        this.view?.setPadding(0, 0, 0, bottomPadding)

        // Ukrywanie toolbar i bottom navigation
        (activity as MainActivity).supportActionBar?.hide()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE


        val factory = TaskViewModelFactory((requireActivity().application as MyApp).taskRepository)
        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        val saveButton = view.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val taskTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val taskDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val taskDate = view.findViewById<EditText>(R.id.editTextDate)
        val chosenGroup = view.findViewById<Spinner>(R.id.spinnerGroup)

        val taskID: Int = arguments?.getInt("taskID") ?: -1
        println(taskID)

        if (taskID != -1){
            viewModel.getTaskById(taskID).observe(viewLifecycleOwner) { task ->
                taskTitle.setText(task.title)
                taskDescription.setText(task.description)
                taskDate.setText(task.date)
            }
        }

        taskDate.setOnClickListener {
            showDatePickerDialog(requireContext()) { year, month, dayOfMonth ->
                val dateString = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
                taskDate.setText(dateString)
            }
        }



        saveButton.setOnClickListener {
            val title: String = taskTitle.text.toString()
            val description: String = taskDescription.text.toString()
            val datetext: String = taskDate.text.toString()
            //val chosengroup: String = chosenGroup.
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
                        groupId = 0
                    )
                    viewModel.updateTask(updatedtask)
                } else {
                    val newtask = Task(
                        title = title,
                        description = description,
                        date = datetext,
                        isCompleted = false,
                        groupId = 0
                    )
                    viewModel.addTask(newtask)
                }
                // Navigacja z powrotem do listy zadań lub poprzedniego ekranu
                findNavController().popBackStack()  // Adjust the ID
            }
        }

        buttonCancel.setOnClickListener {
            // Navigacja z powrotem do listy zadań lub poprzedniego ekranu
            findNavController().popBackStack()
        }
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

    fun setupSpinner(spinner: Spinner, groups: LiveData<List<Group>>) {
        val adapter = ArrayAdapter<String>(spinner.context, android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        groups.observe(viewLifecycleOwner, Observer { groupList ->
            adapter.clear()
            adapter.addAll(groupList.map { it.name })
            adapter.notifyDataSetChanged()
        })
    }



//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment AddTaskFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AddTaskFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_TITLE, param1)
//                    putString(ARG_DESCRIPTION, param2)
//                }
//            }
//    }
}