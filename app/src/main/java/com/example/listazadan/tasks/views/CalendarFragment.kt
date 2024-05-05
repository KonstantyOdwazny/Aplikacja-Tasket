package com.example.listazadan.tasks.views

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.ParseException
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Task
import com.example.listazadan.databinding.FragmentCalendarBinding
import com.example.listazadan.tasks.viewmodel.TaskViewModel
import com.example.listazadan.tasks.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarView: CalendarView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val taskFactory = TaskViewModelFactory((requireActivity().application as MyApp).taskRepository)
        taskViewModel = ViewModelProvider(this, taskFactory)[TaskViewModel::class.java]

        calendarView = binding.calendarView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
            updateCalendarWithTasks(tasks)
        })

        try {
            val topPadding = resources.getDimensionPixelSize(R.dimen.toolbar_height) // Definiujesz w dimens.xml
            val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height) // Definiujesz w dimens.xml
            view.setPadding(0, topPadding, 0, bottomPadding)
        }catch (e: Resources.NotFoundException) {
            Log.e("TAG3", "Resource not found: " + e.message)
        }

        /// Ustawienie zdarzeń w kalendarzu
        val events = mutableListOf<CalendarDay>()

        // Dodaj zdarzenia do kalendarza
        val calendar = Calendar.getInstance()
        events.add(CalendarDay(calendar))

        calendarView.setCalendarDays(events)
    }

    private fun updateCalendarWithTasks(tasks: List<Task>) {

        //TODO Add calendar ICons
        val events = mutableListOf<CalendarDay>()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())  // Asumując, że data jest w tym formacie

        tasks.forEach { task ->
            if (task.date?.isNotBlank() == true) {  // Sprawdzenie, czy data nie jest pusta
                try {
                    val calendar = Calendar.getInstance()
                    calendar.time = format.parse(task.date)!!  // Parsowanie daty
                    events.add(CalendarDay(calendar))  // Dodanie zdarzenia do kalendarza
                } catch (e: ParseException) {
                    Log.e("CalendarFragment", "Error parsing date: ${task.date}", e)
                }
            }
        }

        calendarView.setCalendarDays(events)
    }

}