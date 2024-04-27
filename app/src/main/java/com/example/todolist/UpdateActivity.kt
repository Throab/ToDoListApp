package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.todolist.databinding.ActivityUpdateBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var adapter: NotesAdapter
    private var noteId: Int = -1
    val today = Calendar.getInstance()
    val timeFormater = SimpleDateFormat("HH:mm")
    val dateFormater = SimpleDateFormat("MM/dd/yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentDate = dateFormater.format(today.time)
        val currentTime = timeFormater.format(today.time)
        val cDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val cTime = LocalTime.parse(currentTime, DateTimeFormatter.ofPattern("HH:mm"))
        val t = findViewById<TextView>(R.id.updateTitleEditText)
        val c = findViewById<TextView>(R.id.updateContentEditText)
        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)
        if(noteId == -1){
            finish()
            return
        }
        val note = db. getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)
        binding.updateTimeTextView.setText(note.time)
        binding.updateDateTextView.setText(note.date)

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener{view, hour, minute ->
                today.set(Calendar.HOUR_OF_DAY, hour)
                today.set(Calendar.MINUTE, minute)
                updateTimeInView()

            }
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                today.set(Calendar.YEAR, year)
                today.set(Calendar.MONTH, monthOfYear)
                today.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        binding.updateTimeEditButton.setOnClickListener {
            TimePickerDialog(this, timeSetListener,
                today.get(Calendar.HOUR_OF_DAY),
                today.get(Calendar.MINUTE), false).show()
        }
        binding.updateDateEditButton.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)).show()
        }




        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val newTime = binding.updateTimeTextView.text.toString()
            val newDate = binding.updateDateTextView.text.toString()
            if(newTitle == "" || newContent == ""||
                cDate.isAfter( LocalDate.parse(newDate,DateTimeFormatter.ofPattern("MM/dd/yyyy")))||
                (cDate == LocalDate.parse(newDate,DateTimeFormatter.ofPattern("MM/dd/yyyy")) &&
                        !cTime.isBefore(LocalTime.parse(newTime,DateTimeFormatter.ofPattern("HH:mm"))))
            ){

                if(newTitle == ""){
                    t.setBackgroundResource(R.drawable.red_border)
                    t.clearFocus()
                }else{
                    t.setBackgroundResource(R.drawable.green_border)
                }
                if(newContent==""){
                    c.setBackgroundResource(R.drawable.red_border)
                    c.clearFocus()
                }else{
                    c.setBackgroundResource(R.drawable.green_border)
                }
                if(!cTime.isBefore(LocalTime.parse(newTime,DateTimeFormatter.ofPattern("HH:mm")))){
                    binding.updateTimeTextView.setBackgroundResource(R.drawable.red_border)
                }else{
                    binding.updateTimeTextView.setBackgroundResource(R.drawable.green_border)
                }
                if(cDate.isAfter( LocalDate.parse(newDate,DateTimeFormatter.ofPattern("MM/dd/yyyy")))){
                    binding.updateDateTextView.setBackgroundResource(R.drawable.red_border)
                }else if(cDate == LocalDate.parse(newDate,DateTimeFormatter.ofPattern("MM/dd/yyyy"))){
                    if(!cTime.isBefore(LocalTime.parse(newTime,DateTimeFormatter.ofPattern("HH:mm")))){
                        binding.updateTimeTextView.setBackgroundResource(R.drawable.red_border)
                    }
                }else{
                    binding.updateDateTextView.setBackgroundResource(R.drawable.green_border)
                }

                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }else{
                val updateNote = Note(noteId, newTitle,newContent, newTime, newDate, 0)
                db.updateNote(updateNote)
                adapter.refreshData(db.getAllNote())
                finish()
                Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            }


        }
    }
    private fun updateTimeInView(){
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.updateTimeTextView.setText(sdf.format(today.getTime()))
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.updateDateTextView.setText(sdf.format(today.getTime()))
    }
}