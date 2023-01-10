package com.example.myapplication

import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking

class NoteRepository(private val notesDao: NotesDao, serverApi: ServerApi, userId: Long) {
    val allNotes: LiveData<List<Note>> = notesDao.getAllNotes()

    init {
        val call = serverApi.getNotesByUserId(userId)
        call.enqueue(object : retrofit2.Callback<List<NoteDto>> {
            override fun onResponse(
                call: retrofit2.Call<List<NoteDto>>,
                response: retrofit2.Response<List<NoteDto>>
            ) {
                val allNotesDto = response.body()!!
                for (noteDto in allNotesDto) {
                    val callImage = serverApi.getNoteImageByNoteId(noteDto.id!!)
                    callImage.enqueue(object : retrofit2.Callback<NoteImageDto> {
                        override fun onResponse(
                            call: retrofit2.Call<NoteImageDto>,
                            response: retrofit2.Response<NoteImageDto>
                        ) {
                            val noteImageDto = response.body()!!
                            runBlocking { notesDao.insert(Note.fromDto(noteDto, noteImageDto)) }
                        }

                        override fun onFailure(
                            call: retrofit2.Call<NoteImageDto>,
                            t: Throwable
                        ) {
                            runBlocking { notesDao.insert(Note.fromDto(noteDto, null)) }
                        }
                    })

                }

            }

            override fun onFailure(call: retrofit2.Call<List<NoteDto>>, t: Throwable) {
            }
        })
    }

    suspend fun insert(note: Note): Long {
        return notesDao.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    suspend fun deleteAll() {
        notesDao.deleteAllNotes()
    }
}