package com.server.server.repository;

import com.server.server.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserId(Long userId);

}
