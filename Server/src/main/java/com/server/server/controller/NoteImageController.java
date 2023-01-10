package com.server.server.controller;

import com.server.server.model.NoteImage;
import com.server.server.service.NoteImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteImageController {

    private final NoteImageService noteImageService;

    @GetMapping("/findNoteImageByNoteId/{noteId}")
    public NoteImage findProductImageByProductId(@PathVariable Long noteId) {
        return noteImageService.findByNoteId(noteId);
    }

    @DeleteMapping("/deleteNoteImage/{imageId}")
    public void deleteProductImage(@PathVariable Long imageId) {
        noteImageService.delete(imageId);
    }

    @PostMapping(path = "/saveNoteImage/{noteId}")
    public NoteImage saveProductImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable Long noteId) throws IOException {
        var noteImage = new NoteImage();
        noteImage.setImage(multipartFile.getBytes());
        noteImage.setId(noteId);
        return noteImageService.save(noteImage);
    }
}
