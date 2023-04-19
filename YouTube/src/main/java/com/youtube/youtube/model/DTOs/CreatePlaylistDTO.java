package com.youtube.youtube.model.DTOs;

import com.youtube.youtube.service.AbstractService;
import com.youtube.youtube.service.VideoService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class CreatePlaylistDTO {
    @NotBlank (message = "Name is mandatory!")
    @Size(max = AbstractService.MAX_TITLE_LENGTH, message = AbstractService.TITLE_TOO_LONG)
    private String name;
    @Size(max = AbstractService.MAX_DESCRIPTION_LENGTH, message = AbstractService.DESCRIPTION_TOO_LONG)
    private String description;
    @NotNull (message = "Visibility is mandatory!")
    private int visibilityId;
}
