package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SortPlaylistDTO {
    private String orderBy;
    private String order;
}
