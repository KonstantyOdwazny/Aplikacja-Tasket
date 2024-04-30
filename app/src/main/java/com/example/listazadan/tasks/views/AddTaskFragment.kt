package com.example.listazadan.tasks.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Task
import com.example.listazadan.tasks.viewmodel.TaskViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModelFactory


class AddTaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = TaskViewModelFactory((requireActivity().application as MyApp).taskRepository)
        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        val saveButton = view.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val taskTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val taskDescription = view.findViewById<EditText>(R.id.editTextDescription)

        val taskID: Int = arguments?.getInt("taskID") ?: -1
        println(taskID)

        if (taskID != -1){
            viewModel.getTaskById(taskID).observe(viewLifecycleOwner) { task ->
                taskTitle.setText(task.title)
                taskDescription.setText(task.description)
            }
        }



        saveButton.setOnClickListener {
            val title = taskTitle.text.toString()
            val description = taskDescription.text.toString()
            if (taskID != -1) {
                val updatedtask = Task(id = taskID,title = title, description = description, isCompleted = false)
                viewModel.updateTask(updatedtask)
            }
            else{
                val newtask = Task(title = title, description = description, isCompleted = false)
                viewModel.addTask(newtask)
            }
            // Navigacja z powrotem do listy zadań lub poprzedniego ekranu
            findNavController().popBackStack()  // Adjust the ID

        }

        buttonCancel.setOnClickListener {
            // Navigacja z powrotem do listy zadań lub poprzedniego ekranu
            findNavController().popBackStack()
        }
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