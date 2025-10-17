package com.example.simplenotesapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var noteEditText: EditText
    private lateinit var addButton: Button
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private val notesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация UI
        noteEditText = findViewById(R.id.noteEditText)
        addButton = findViewById(R.id.addButton)
        notesRecyclerView = findViewById(R.id.notesRecyclerView)

        // Настройка RecyclerView
        adapter = NoteAdapter(notesList)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = adapter

        // Восстановление состояния
        if (savedInstanceState != null) {
            val savedNotes = savedInstanceState.getStringArrayList("notes")
            if (savedNotes != null) {
                notesList.addAll(savedNotes)
                adapter.notifyDataSetChanged()
            }
        }

        // Добавление заметки
        addButton.setOnClickListener {
            val noteText = noteEditText.text.toString()
            if (noteText.isNotBlank()) {
                notesList.add(noteText)
                adapter.notifyItemInserted(notesList.size - 1)
                noteEditText.text.clear()
            }
        }

        // Swipe для удаления
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                notesList.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    // Сохранение состояния при прерываниях
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("notes", ArrayList(notesList))
    }
}