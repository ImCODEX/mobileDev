package com.server.server.service;

import com.server.server.model.Note;

import java.util.List;

public interface INoteService {
    List<Note> getAllNotes();

    Note addNote(Note note);

    Note updateNote(Note note);

    void deleteNote(Long id);

    Note getNoteById(Long id);

    List<Note> getAllNotesByUserId(Long userId);

}
