package com.server.server.repository;

import com.server.server.model.NoteImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteImageRepository extends JpaRepository<NoteImage, Long> {
    NoteImage findByNoteId(Long id);
}
