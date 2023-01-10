package com.server.server.converter;


import com.server.server.dto.NoteImageDto;
import com.server.server.model.NoteImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteImageConverter implements IConverter<NoteImage, NoteImageDto> {

    @Override
    public NoteImage convertDtoToModel(NoteImageDto noteImageDto) {
        var noteImage = new NoteImage();
        noteImage.setId(noteImageDto.getId());
        noteImage.setImage(noteImageDto.getImage());
        return noteImage;
    }

    @Override
    public NoteImageDto convertModelToDto(NoteImage noteImage) {
        if (noteImage == null) {
            return new NoteImageDto();
        }
        var noteImageDto = new NoteImageDto();
        noteImageDto.setId(noteImage.getId());
        noteImageDto.setImage(noteImage.getImage());
        return noteImageDto;
    }

    @Override
    public List<NoteImageDto> convertModelListToDtoList(List<NoteImage> noteImages) {
        return noteImages.stream()
                .map(this::convertModelToDto)
                .collect(java.util.stream.Collectors.toList());
    }
}
