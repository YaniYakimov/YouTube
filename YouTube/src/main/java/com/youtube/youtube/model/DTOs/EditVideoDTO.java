package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class EditVideoDTO {
    private String name;
    private String description;
    private int visibilityId;
    private int categoryId;
}
