package com.youtube.youtube.model.DTOs;


import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.Video;
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
//    private Set<VideoWithoutOwnerDTO> videos;
    private int videosCount;

}
