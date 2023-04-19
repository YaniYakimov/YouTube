package com.youtube.youtube.model.DTOs;

import com.youtube.youtube.service.AbstractService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class EditVideoDTO {
    @NotBlank(message = "Name is mandatory!")
    @Size(max = AbstractService.MAX_TITLE_LENGTH, message = AbstractService.TITLE_TOO_LONG)
    private String name;
    @NotBlank(message = "Description is mandatory!")
    @Size(max = AbstractService.MAX_DESCRIPTION_LENGTH, message = AbstractService.DESCRIPTION_TOO_LONG)
    private String description;
    @NotNull
    private int visibilityId;
    @NotNull
    private int categoryId;
}
