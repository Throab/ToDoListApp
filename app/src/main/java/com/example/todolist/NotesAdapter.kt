package com.example.todolist

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class NotesAdapter(private var notes: List<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>()
{
    private lateinit var main:MainActivity
    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById((R.id.titleTextView))
        val contentTextView: TextView = itemView.findViewById((R.id.contentTextView))
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        val completedButton: ImageView = itemView.findViewById(R.id.completeButton)
        val isPassedDue: TextView = itemView.findViewById(R.id.isPassedDue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_items, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        val today = Calendar.getInstance()
        val timeFormater = SimpleDateFormat("HH:mm")
        val dateFormater = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = dateFormater.format(today.time)
        val currentTime = timeFormater.format(today.time)
        val cDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val cTime = LocalTime.parse(currentTime, DateTimeFormatter.ofPattern("HH:mm"))
        if(cDate.isAfter(LocalDate.parse(note.date, DateTimeFormatter.ofPattern("MM/dd/yyyy"))) ){
            holder.isPassedDue.visibility = View.VISIBLE
        }else if(cDate == LocalDate.parse(note.date, DateTimeFormatter.ofPattern("MM/dd/yyyy"))){
            if(cTime.isAfter(LocalTime.parse(note.time, DateTimeFormatter.ofPattern("HH:mm"))) ){
                holder.isPassedDue.visibility = View.VISIBLE
            }
        }else{
            holder.isPassedDue.visibility = View.GONE
        }

        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.timeTextView.text = note.time
        holder.dateTextView.text = note.date
        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply{
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener{
            db.deleteNote(note.id)
            refreshData(db.getAllNote())
            Toast.makeText(holder.itemView.context, "Deleted", Toast.LENGTH_SHORT).show()
        }
        holder.completedButton.setOnClickListener{
            db.checkComplete(note.id)
            refreshData(db.getAllNote())
            Toast.makeText(holder.itemView.context, "Marked as done", Toast.LENGTH_SHORT).show()
        }
    }
    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }
}