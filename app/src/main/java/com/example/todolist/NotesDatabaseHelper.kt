package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "noteapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DONE = "done"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_TIME TEXT, $COLUMN_DATE TEXT, $COLUMN_DONE INT)"
        db?.execSQL((createTableQuery))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertNote(note: Note){
        val db = writableDatabase;
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_TIME, note.time)
            put(COLUMN_DATE, note.date)
            put(COLUMN_DONE, note.done)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllNote() : List<Note>{
        val noteList = mutableListOf<Note>()
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DONE = 0 ORDER BY $COLUMN_DATE, $COLUMN_TIME"
        val cursor = db.rawQuery(query, null)
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DONE))
            Log.i("Done", done.toString())
            val note = Note(id, title, content, time, date, done)
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
    }
    fun getAllCompletedNote() : List<Note>{
        val noteList = mutableListOf<Note>()
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DONE = 1 ORDER BY $COLUMN_DATE, $COLUMN_TIME"
        val cursor = db.rawQuery(query, null)
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DONE))
            Log.i("Done", done.toString())
            val note = Note(id, title, content, time, date, done)
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
    }
    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_TIME, note.time)
            put(COLUMN_DATE, note.date)
            put(COLUMN_DONE, note.done)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
    fun getNoteByID(noteId: Int):Note{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        val done = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DONE))
        cursor.close()
        db.close()
        return Note(id, title, content, time, date, done)
    }
     fun deleteNote(noteId: Int){
         val db =  writableDatabase
         val whereClause = "$COLUMN_ID = ?"
         val whereArgs = arrayOf(noteId.toString())
         db.delete(TABLE_NAME, whereClause, whereArgs)
         db.close()
     }

    fun deleteCompletedNote(noteId: Int){
        val db =  writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
    fun checkComplete(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ? "
        val whereArgs = arrayOf(noteId.toString())
        val values = ContentValues().apply{
            put(COLUMN_DONE, 1)
        }
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
    fun getDueDate(): List<String>{
        val dateList = mutableListOf<String>()
        val db = writableDatabase
        val query = "SELECT $COLUMN_DATE FROM $TABLE_NAME WHERE $COLUMN_DONE = 0 ORDER BY $COLUMN_DATE, $COLUMN_TIME"
        val cursor = db.rawQuery(query, null)
        while(cursor.moveToNext()){
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            dateList.add(date)
        }
        cursor.close()
        db.close()
        return dateList
    }
}