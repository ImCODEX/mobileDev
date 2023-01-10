package com.example.myapplication

data class NoteDto(
    val id: Long?,
    val noteTitle: String?,
    val noteDescription: String?,
    val timeStamp: String?,
    val firstTimeStamp: String?,
    val userId: Long?
)