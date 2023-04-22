package com.youtube.youtube.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private UserBasicInfoDTO user;
    private String name;
    private String description;
    private int views;
    private String playlistUrl;
    private VisibilityDTO visibility;
    private Set<VideoWithoutOwnerDTO> videos;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
}
