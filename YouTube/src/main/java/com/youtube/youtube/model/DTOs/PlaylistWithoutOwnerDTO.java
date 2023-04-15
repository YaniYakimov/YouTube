package com.youtube.youtube.model.DTOs;




public record PlaylistWithoutOwnerDTO (int id, String name, String playlistUrl, int visibility, int videos) {
}
