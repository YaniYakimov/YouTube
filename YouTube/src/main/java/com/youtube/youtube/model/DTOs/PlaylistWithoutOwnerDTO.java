package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class PlaylistWithoutOwnerDTO {
    private int id;
    private String name;
    private int videosCount;

}
