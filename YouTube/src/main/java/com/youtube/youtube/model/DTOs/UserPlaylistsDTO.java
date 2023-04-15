package com.youtube.youtube.model.DTOs;

import java.util.Set;

public record UserPlaylistsDTO (int id, String firstName, String lastName, Set<PlaylistWithoutOwnerDTO> videos) {
}
