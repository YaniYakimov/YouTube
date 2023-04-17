package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor

public class UserPlaylistsDTO {
    private int id;
    private String firstName;
    private String lastName;
    private Set<PlaylistWithoutOwnerDTO> playlists;

}
