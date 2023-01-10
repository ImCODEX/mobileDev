package com.server.server.converter;

import com.server.server.dto.NoteDto;
import com.server.server.model.Note;
import com.server.server.model.Users;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class NoteConverter implements IConverter<Note, NoteDto> {

    @Override
    public Note convertDtoToModel(NoteDto noteDto) {
        var note = Note.builder()
                .id(noteDto.getId())
                .title(noteDto.getNoteTitle())
                .description(noteDto.getNoteDescription())
                .timestamp(noteDto.getTimeStamp())
                .firstTimestamp(noteDto.getFirstTimeStamp())
                .build();
        var user = new Users();
        user.setId(noteDto.getUserId());
        note.setUser(user);
        return note;
    }

    @Override
    public NoteDto convertModelToDto(Note note) {
        if (note == null) {
            return new NoteDto();
        }

        return NoteDto.builder()
                .id(note.getId())
                .noteTitle(note.getTitle())
                .noteDescription(note.getDescription())
                .timeStamp(note.getTimestamp())
                .firstTimeStamp(note.getFirstTimestamp())
                .userId(note.getUser().getId())
                .build();
    }

    @Override
    public List<NoteDto> convertModelListToDtoList(List<Note> notes) {
        return notes.stream()
                .map(this::convertModelToDto)
                .toList();
    }
}
