package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class PlaylistSortDTO {
    private int id;
    private UserBasicInfoDTO user;
    private String name;
    private String description;
    private int views;
    private String playlistUrl;
    private List<VideoWithoutOwnerDTO> sortedVideos;
}
