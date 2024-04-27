package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityCompletedTasksBinding

class CompletedTasksActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: CompletedNotesAdapter
    private lateinit var binding: ActivityCompletedTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompletedTasksBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = NotesDatabaseHelper(this)
        notesAdapter = CompletedNotesAdapter(db.getAllCompletedNote(), this)
        toolbar = findViewById(R.id.completedToolBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Completed Tasks"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.completedNotesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.completedNotesRecyclerView.adapter = notesAdapter
    }
    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllCompletedNote())
    }
}