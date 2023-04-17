package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor

public class PlaylistInfoDTO {
    private int id;
    private UserBasicInfoDTO owner;
    private LocalDateTime dateCreated;
    private String name;
    private String description;
    private int views;
    private String playlistUrl;
    private int visibility;
    private Set<VideoWithoutOwnerDTO> videos;
}
