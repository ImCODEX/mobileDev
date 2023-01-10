package com.example.myapplication

import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Base64

@Entity(tableName = "notesTable")
class Note(
    @ColumnInfo(name = "title") val noteTitle: String,
    @ColumnInfo(name = "description") val noteDescription: String,
    @ColumnInfo(name = "timestamp") val timeStamp: String,
    @ColumnInfo(name = "first_timestamp") val firstTimeStamp: String,
    val image: ByteArray
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    fun toDto(id: Long, userId: Long): NoteDto {
        return NoteDto(
            id,
            noteTitle,
            noteDescription,
            timeStamp,
            firstTimeStamp,
            userId
        )
    }

    companion object {
        fun fromDto(noteDto: NoteDto, noteImageDto: NoteImageDto?): Note {
            val note = noteDto.noteTitle?.let { title ->
                noteDto.noteDescription?.let { description ->
                    noteDto.timeStamp?.let { timestamp ->
                        noteDto.firstTimeStamp?.let { firstTimestamp ->
                            Note(
                                title,
                                description,
                                timestamp,
                                firstTimestamp,
                                android.util.Base64.decode(noteImageDto?.image ?: "", android.util.Base64.DEFAULT)
                            )
                        }
                    }
                }
            }
            if (note != null) {
                note.id = noteDto.id!!
            }
            return note!!
        }
    }

    override fun toString(): String {
        return "Note(noteTitle='$noteTitle', noteDescription='$noteDescription', timeStamp='$timeStamp', firstTimeStamp='$firstTimeStamp', id=$id)"
    }
}