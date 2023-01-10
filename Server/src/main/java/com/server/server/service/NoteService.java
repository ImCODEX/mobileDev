package com.server.server.service;

import com.server.server.exceptions.NoteNotFoundException;
import com.server.server.model.Note;
import com.server.server.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteService implements INoteService {

    private final NotesRepository noteRepository;

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note addNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Note note) {
        noteRepository.findById(note.getId()).orElseThrow(NoteNotFoundException::new);
        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.findById(id).orElseThrow(NoteNotFoundException::new);
        noteRepository.deleteById(id);
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElseThrow(NoteNotFoundException::new);
    }

    @Override
    public List<Note> getAllNotesByUserId(Long userId) {
        return noteRepository.findAllByUserId(userId);
    }
}
