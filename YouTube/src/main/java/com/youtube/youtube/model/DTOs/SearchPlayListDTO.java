package com.youtube.youtube.model.DTOs;

import java.time.LocalDateTime;


public record SearchPlayListDTO (int id, UserBasicInfoDTO owner, String name, String playlistUrl, int videos) {
}
