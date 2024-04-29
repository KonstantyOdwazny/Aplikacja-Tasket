package com.example.listazadan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.listazadan.database.Task
import com.example.listazadan.viewmodel.TaskViewModel
import com.example.listazadan.viewmodel.TaskViewModelFactory

class AddTaskActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory = TaskViewModelFactory((application as MyApp).taskRepository)
        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        val saveButton = findViewById<Button>(R.id.buttonSave)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)
        val taskTitle = findViewById<EditText>(R.id.editTextTitle)
        val taskDescription = findViewById<EditText>(R.id.editTextDescription)

        saveButton.setOnClickListener {
            val title = taskTitle.text.toString()
            val description = taskDescription.text.toString()
            val task = Task(title = title, description = description, isCompleted = false)
            viewModel.addTask(task)
            finish()
        }

        buttonCancel.setOnClickListener {
            finish() // Zamknij aktywność bez zapisywania
        }
    }
}