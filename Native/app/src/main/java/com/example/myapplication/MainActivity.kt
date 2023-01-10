package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates
import androidx.core.util.Pair as APair

class MainActivity : AppCompatActivity(), NoteClickDeleteInterface, NoteClickInterface {
    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModal: NoteViewModal
    var userId by Delegates.notNull<Long>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesRV = findViewById(R.id.idRVNotes)
        addFAB = findViewById(R.id.idFABAddNote)
        notesRV.layoutManager = LinearLayoutManager(this)

        val noteRVAdapter = NoteRVAdapter(this, this, this)
        notesRV.adapter = noteRVAdapter
        userId = intent.getLongExtra("userId", -1)
        viewModal = ViewModelProvider(
            this,
            NoteViewModalFactory(application, userId)
        )[NoteViewModal::class.java]
        viewModal.allNotes.observe(this) { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        }
        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            this.finish()
        }

        val searchView = findViewById<SearchView>(R.id.searchView)


        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean{
                val list = viewModal.allNotes.value
                val filteredList = list?.filter { it.noteTitle.contains(newText, ignoreCase = true) || it.noteDescription.contains(newText, ignoreCase = true) }
                if (filteredList != null) {
                    noteRVAdapter.updateList(filteredList)
                }
                return true
            }

            override fun onQueryTextSubmit(newText: String): Boolean{
                val list = viewModal.allNotes.value
                val filteredList = list?.filter { it.noteTitle.contains(newText, ignoreCase = true) || it.noteDescription.contains(newText, ignoreCase = true)}
                if (filteredList != null) {
                    noteRVAdapter.updateList(filteredList)
                }
                return true
            }
        })

        val myFormat = "dd/MM/yyyy"
        val formattedDate = "01/01/2023"

        val sdf = SimpleDateFormat(myFormat)
        val date = sdf.parse(formattedDate)
        val timeInMillis = date.time

        val constraintBuilder = CalendarConstraints.Builder().setOpenAt(
            timeInMillis //pass time in milli seconds
        ).build()

        val utc: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val format = SimpleDateFormat("yyyy-MM-dd")

        val calendar = findViewById<Button>(R.id.calendar)
        val materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select dates").setSelection(APair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).setCalendarConstraints(constraintBuilder).build()
        calendar.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                materialDatePicker.show(supportFragmentManager, "datePicker")
            }
        })

        materialDatePicker.addOnPositiveButtonClickListener {
            val date1 = Date(it.first)
            val date2 = Date(it.second)

            val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
            val list = viewModal.allNotes.value
            val filteredList = list?.filter { sdf.parse(it.firstTimeStamp)!! > date1 && sdf.parse(it.firstTimeStamp)!! < date2 }
            if (filteredList != null) {
                noteRVAdapter.updateList(filteredList)
            }
        }

    }

    override fun onDeleteIconClick(note: Note) {
        viewModal.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteID", note.id)
        intent.putExtra("firstTimeStamp", note.firstTimeStamp)
        intent.putExtra("image", note.image)
        intent.putExtra("userId", userId)
        startActivity(intent)
        this.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val noteRVAdapter = NoteRVAdapter(this, this, this)
        notesRV.adapter = noteRVAdapter

        if (item.itemId == R.id.logout) {
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            lifecycleScope.launch {
                viewModal.repository.deleteAll()
            }
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        if(item.itemId == R.id.sort_asc){
            val list = viewModal.allNotes.value
            val sortedList = list?.sortedWith(compareBy {it.timeStamp})

            if (sortedList != null) {
                noteRVAdapter.updateList(sortedList)
            }
        }

        if(item.itemId == R.id.sort_desc){
            val list = viewModal.allNotes.value
            val sortedList = list?.sortedWith(compareByDescending {it.timeStamp})

            if (sortedList != null) {
                noteRVAdapter.updateList(sortedList)
            }
        }

        return true
    }
}