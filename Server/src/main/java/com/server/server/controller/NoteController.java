package com.server.server.controller;

import com.server.server.converter.NoteConverter;
import com.server.server.dto.NoteDto;
import com.server.server.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteController {
    private final NoteService noteService;
    private final NoteConverter noteConverter;

    @PostMapping("/addNote")
    public NoteDto addNote(@RequestBody NoteDto noteDto) {
        var note = noteConverter.convertDtoToModel(noteDto);
        var addedNote = noteService.addNote(note);
        return noteConverter.convertModelToDto(addedNote);
    }

    @PutMapping("/updateNote")
    public NoteDto updateNote(@RequestBody NoteDto noteDto) {
        var note = noteConverter.convertDtoToModel(noteDto);
        var updatedNote = noteService.updateNote(note);
        return noteConverter.convertModelToDto(updatedNote);
    }

    @DeleteMapping("/deleteNote/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

    @GetMapping("/findNoteById/{id}")
    public NoteDto findNoteById(@PathVariable Long id) {
        var note = noteService.getNoteById(id);
        return noteConverter.convertModelToDto(note);
    }

    @GetMapping("/findAllNotes")
    public List<NoteDto> findAllNotes() {
        var notes = noteService.getAllNotes();
        return noteConverter.convertModelListToDtoList(notes);
    }

    @GetMapping("/findAllNotesByUserId/{userId}")
    public List<NoteDto> findAllNotesByUserId(@PathVariable Long userId) {
        var notes = noteService.getAllNotesByUserId(userId);
        return noteConverter.convertModelListToDtoList(notes);
    }
}
