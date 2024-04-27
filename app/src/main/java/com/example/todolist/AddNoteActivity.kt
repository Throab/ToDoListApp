package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.todolist.databinding.ActivityAddNoteBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

private lateinit var binding: ActivityAddNoteBinding
private lateinit var db:NotesDatabaseHelper
class AddNoteActivity : AppCompatActivity() {
    val today = Calendar.getInstance()
//    val timeFormater = SimpleDateFormat("HH:mm")
//    val dateFormater = SimpleDateFormat("MM/dd/yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val currentDate = dateFormater.format(today.time)
//        val currentTime = timeFormater.format(today.time)
//        val cDate = LocalDate.parse(currentDate,DateTimeFormatter.ofPattern("MM/dd/yyyy"))
//        val cTime = LocalTime.parse(currentTime,DateTimeFormatter.ofPattern("HH:mm"))
        db = NotesDatabaseHelper(this)
        val t = findViewById<TextView>(R.id.addTitleEditText)
        val c = findViewById<TextView>(R.id.addContentEditText)
        binding.saveButton.setOnClickListener{

            val title = binding.addTitleEditText.text.toString()
            val content = binding.addContentEditText.text.toString()
            val time = binding.addTimeTextView.text.toString()
            val date = binding.addDateTextView.text.toString()
            if(title == "" || content == ""|| time =="--/--" || date =="--/--/----"){

                if(title == ""){
                    t.setBackgroundResource(R.drawable.red_border)
                    t.clearFocus()
                }else{
                    t.setBackgroundResource(R.drawable.green_border)
                }
                if(content==""){
                    c.setBackgroundResource(R.drawable.red_border)
                    c.clearFocus()
                }else{
                    c.setBackgroundResource(R.drawable.green_border)
                }
                if(time =="--/--"){
                    binding.addTimeTextView.setBackgroundResource(R.drawable.red_border)
                }else{
                    binding.addTimeTextView.setBackgroundResource(R.drawable.green_border)
                }
                if(date =="--/--/----"){
                    binding.addDateTextView.setBackgroundResource(R.drawable.red_border)
                }else{
                    binding.addDateTextView.setBackgroundResource(R.drawable.green_border)
                }

                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }
            else{
                val note = Note(0, title, content, time, date, 0)
                db.insertNote(note)
                finish()
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()

            }

        }
        binding.addTimeTextView.setText("--/--")
        binding.addDateTextView.setText("--/--/----")

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

        binding.addTimeEditButton.setOnClickListener {
            TimePickerDialog(this, timeSetListener,
            today.get(Calendar.HOUR_OF_DAY),
            today.get(Calendar.MINUTE), false).show()
        }
        binding.addDateEditButton.setOnClickListener {
            DatePickerDialog(this,
            dateSetListener,
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)).show()
        }

    }
    private fun updateTimeInView(){
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.addTimeTextView.setText(sdf.format(today.getTime()))
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.addDateTextView.setText(sdf.format(today.getTime()))
    }
}