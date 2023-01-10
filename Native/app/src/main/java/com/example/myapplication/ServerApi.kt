package com.example.myapplication

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServerApi {
    @GET("findAllNotesByUserId/{userId}")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun getNotesByUserId(@Path("userId") userId: Long): Call<List<NoteDto>>

    @POST("addNote")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun addNote(@Body note: NoteDto): Call<NoteDto>

    @DELETE("deleteNote/{id}")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun deleteNote(@Path("id") id: Long): Call<Void>

    @PUT("updateNote")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun updateNote(@Body note: NoteDto): Call<NoteDto>

    @Multipart
    @POST("login")
    fun login(@Part image: MultipartBody.Part): Call<Long>

    @GET("findNoteImageByNoteId/{noteId}")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun getNoteImageByNoteId(@Path("noteId") noteId: Long): Call<NoteImageDto>

    @DELETE("deleteNoteImage/{id}")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun deleteNoteImage(@Path("id") id: Long): Call<Void>

    @Multipart
    @POST("saveNoteImage/{noteId}")
    fun saveNoteImage(@Path("noteId") noteId: Long, @Part image: MultipartBody.Part): Call<NoteImageDto>
}