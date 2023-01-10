package com.server.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteDto {
    private Long id;
    private String noteTitle;
    private String noteDescription;
    private String timeStamp;
    private String firstTimeStamp;
    private Long userId;
    private String image;
}
