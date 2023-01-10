package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.properties.Delegates

class NoteViewModal(application: Application, userId: Long) : AndroidViewModel(application) {
    val allNotes: LiveData<List<Note>>
    val repository: NoteRepository
    private val serverApi: ServerApi
    private val userId: Long

    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        serverApi = RetrofitHelper.getInstance().create(ServerApi::class.java)
        this.userId = userId
        repository = NoteRepository(dao, serverApi, userId)
        allNotes = repository.allNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
        val call = serverApi.deleteNoteImage(note.id)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(
                call: retrofit2.Call<Void>,
                response: retrofit2.Response<Void>
            ) {
                val call2 = serverApi.deleteNote(note.id)
                call2.enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(
                        call: retrofit2.Call<Void>,
                        response: retrofit2.Response<Void>
                    ) {
                    }

                    override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                        println(t)
                    }
                })
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                val call2 = serverApi.deleteNote(note.id)
                call2.enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(
                        call: retrofit2.Call<Void>,
                        response: retrofit2.Response<Void>
                    ) {
                    }

                    override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                        println(t)
                    }
                })
            }
        })
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
        val call = serverApi.updateNote(note.toDto(note.id, userId))
        call.enqueue(object : retrofit2.Callback<NoteDto> {
            override fun onResponse(
                call: retrofit2.Call<NoteDto>,
                response: retrofit2.Response<NoteDto>
            ) {
            }

            override fun onFailure(call: retrofit2.Call<NoteDto>, t: Throwable) {
            }
        })
        val call2 = serverApi.deleteNoteImage(note.id)
        call2.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(
                call: retrofit2.Call<Void>,
                response: retrofit2.Response<Void>
            ) {
                val img = MultipartBody.Part.createFormData(
                    "image",
                    "image.png",
                    RequestBody.create(MediaType.parse("image/*"), note.image)
                )
                val callImage = serverApi.saveNoteImage(note.id, img)
                callImage.enqueue(object : retrofit2.Callback<NoteImageDto> {
                    override fun onResponse(
                        call: retrofit2.Call<NoteImageDto>,
                        response: retrofit2.Response<NoteImageDto>
                    ) {
                    }

                    override fun onFailure(call: retrofit2.Call<NoteImageDto>, t: Throwable) {
                    }
                })
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                val img = MultipartBody.Part.createFormData(
                    "image",
                    "image.png",
                    RequestBody.create(MediaType.parse("image/*"), note.image)
                )
                val callImage = serverApi.saveNoteImage(note.id, img)
                callImage.enqueue(object : retrofit2.Callback<NoteImageDto> {
                    override fun onResponse(
                        call: retrofit2.Call<NoteImageDto>,
                        response: retrofit2.Response<NoteImageDto>
                    ) {
                    }

                    override fun onFailure(call: retrofit2.Call<NoteImageDto>, t: Throwable) {
                    }
                })
            }
        })
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        val id = repository.insert(note)
        val call = serverApi.addNote(note.toDto(id, userId))
        call.enqueue(object : retrofit2.Callback<NoteDto> {
            override fun onResponse(
                call: retrofit2.Call<NoteDto>,
                response: retrofit2.Response<NoteDto>
            ) {
                val img = MultipartBody.Part.createFormData(
                    "image",
                    "image.png",
                    RequestBody.create(MediaType.parse("image/*"), note.image)
                )
                val callImage = serverApi.saveNoteImage(id, img)
                callImage.enqueue(object : retrofit2.Callback<NoteImageDto> {
                    override fun onResponse(
                        call: retrofit2.Call<NoteImageDto>,
                        response: retrofit2.Response<NoteImageDto>
                    ) {
                    }

                    override fun onFailure(call: retrofit2.Call<NoteImageDto>, t: Throwable) {
                    }
                })
            }

            override fun onFailure(call: retrofit2.Call<NoteDto>, t: Throwable) {
                val img = MultipartBody.Part.createFormData(
                    "image",
                    "image.png",
                    RequestBody.create(MediaType.parse("image/*"), note.image)
                )
                val callImage = serverApi.saveNoteImage(id, img)
                callImage.enqueue(object : retrofit2.Callback<NoteImageDto> {
                    override fun onResponse(
                        call: retrofit2.Call<NoteImageDto>,
                        response: retrofit2.Response<NoteImageDto>
                    ) {
                    }

                    override fun onFailure(call: retrofit2.Call<NoteImageDto>, t: Throwable) {
                    }
                })
            }
        })



    }
}