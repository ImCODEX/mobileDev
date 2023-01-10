package com.server.server.service;

import com.server.server.exceptions.NoteImageNotFoundException;
import com.server.server.exceptions.NoteNotFoundException;
import com.server.server.model.NoteImage;
import com.server.server.repository.NoteImageRepository;
import com.server.server.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteImageService implements INoteImageService {

    private final NoteImageRepository noteImageRepository;

    private final NotesRepository notesRepository;

    @Override
    public NoteImage findByNoteId(Long id) {
        return this.noteImageRepository.findByNoteId(id);
    }

    @Override
    public NoteImage save(NoteImage noteImage) {
        var note = notesRepository.findById(noteImage.getId()).orElseThrow(NoteNotFoundException::new);
        notesRepository.save(note);
        noteImage.setNote(note);
        return this.noteImageRepository.save(noteImage);
    }

    @Override
    public void delete(Long id) {
        noteImageRepository.findById(id).orElseThrow(NoteImageNotFoundException::new);
        this.noteImageRepository.deleteById(id);
    }
}