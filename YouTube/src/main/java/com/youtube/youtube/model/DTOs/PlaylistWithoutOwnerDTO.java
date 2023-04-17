package com.youtube.youtube.model.DTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor

public class PlaylistWithoutOwnerDTO {
    private int id;
    private String name;
    int videos;
}
