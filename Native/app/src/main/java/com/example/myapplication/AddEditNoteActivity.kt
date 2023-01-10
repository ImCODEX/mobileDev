package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {
    lateinit var noteTitleEdt: EditText
    lateinit var noteDescriptionEdt: EditText
    lateinit var addUpdateBtn: Button
    lateinit var attachmentBtn: ImageView
    lateinit var viewModal: NoteViewModal
    var image: ByteArray = byteArrayOf()
    var noteID: Long = -1
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data!!
                data.data?.let {
                    val inputStream = contentResolver.openInputStream(it)
                    image = inputStream!!.readBytes()
                    Toast.makeText(this, "Image Added", Toast.LENGTH_SHORT).show()
                    val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    val spannable = SpannableString(" ")
                    val ds = BitmapDrawable(
                        resources,
                        Bitmap.createScaledBitmap(imageBitmap, 400, 400, true)
                    )
                    ds.setBounds(0, 0, ds.intrinsicWidth, ds.intrinsicHeight)
                    spannable.setSpan(ImageSpan(ds), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    noteDescriptionEdt.append(spannable)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        noteTitleEdt = findViewById(R.id.idEdtNoteTitle)
        noteDescriptionEdt = findViewById(R.id.idEdtNoteDescription)
        addUpdateBtn = findViewById(R.id.idBtnAddUpdate)
        attachmentBtn = findViewById(R.id.idAttachImage)
        val userId = intent.getLongExtra("userId", -1)
        viewModal = ViewModelProvider(
            this,
            NoteViewModalFactory(application, userId)
        )[NoteViewModal::class.java]

        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDescription")
            val imageBytes = intent.getByteArrayExtra("image")
            noteID = intent.getLongExtra("noteID", -1)
            addUpdateBtn.setText("Update Note")
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDesc)
            image = imageBytes!!
            val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            if (imageBitmap != null) {
                val spannable = SpannableString(" ")
                val ds = BitmapDrawable(
                    resources,
                    Bitmap.createScaledBitmap(imageBitmap, 400, 400, true)
                )
                ds.setBounds(0, 0, ds.intrinsicWidth, ds.intrinsicHeight)
                spannable.setSpan(ImageSpan(ds), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                noteDescriptionEdt.append(spannable)
            }
        } else {
            addUpdateBtn.setText("Save Note")
        }

        addUpdateBtn.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()

            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate: String = sdf.format(Date())
                    val updateNote = Note(
                        noteTitle,
                        noteDescription,
                        currentDate,
                        intent.getStringExtra("firstTimeStamp") ?: "",
                        image
                    )
                    updateNote.id = noteID
                    viewModal.updateNote(updateNote)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate: String = sdf.format(Date())
                    viewModal.addNote(
                        Note(
                            noteTitle,
                            noteDescription,
                            currentDate,
                            currentDate,
                            image
                        )
                    )
                    Toast.makeText(this, "Note Added..", Toast.LENGTH_LONG).show()
                }
            }
            val intent = Intent(this@AddEditNoteActivity, MainActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            this.finish()
        }

        attachmentBtn.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            takeImageResult.launch(photoPickerIntent)
        }

    }
}
