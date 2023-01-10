package com.server.server.service;


import com.server.server.model.NoteImage;

public interface INoteImageService {

    NoteImage findByNoteId(Long id);

    NoteImage save(NoteImage noteImage);

    void delete(Long id);
}