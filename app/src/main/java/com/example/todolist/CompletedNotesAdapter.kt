package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CompletedNotesAdapter(private var notes: List<Note>, context: Context) : RecyclerView.Adapter<CompletedNotesAdapter.NoteViewHolder>()
{
    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById((R.id.titleTextView))
        val contentTextView: TextView = itemView.findViewById((R.id.contentTextView))
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_note_items, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.timeTextView.text = note.time
        holder.dateTextView.text = note.date

        holder.deleteButton.setOnClickListener{
            db.deleteCompletedNote(note.id)
            refreshData(db.getAllCompletedNote())
            Toast.makeText(holder.itemView.context, "Deleted", Toast.LENGTH_SHORT).show()
        }

    }
    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }
}