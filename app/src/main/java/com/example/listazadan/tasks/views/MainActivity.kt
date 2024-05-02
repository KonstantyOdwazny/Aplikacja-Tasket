package com.example.listazadan.tasks.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listazadan.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnClickListener { item ->
            when (item.id) {
                R.id.navigation_groups -> {
                    // Przełącz fragment na Grupy
                    true
                }
                R.id.navigation_tasks -> {
                    // Przełącz fragment na Lista Zadań
                    true
                }
                R.id.navigation_calendar -> {
                    // Przełącz fragment na Kalendarz
                    true
                }
                else -> false
            }
        }

        val toolbar: Toolbar = findViewById(R.id.listtoolbar)
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    // Handle settings action
                    true
                }
                else -> false
            }
        }

    }
}