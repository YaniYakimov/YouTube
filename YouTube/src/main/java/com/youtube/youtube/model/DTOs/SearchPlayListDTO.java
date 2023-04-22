package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class SearchPlayListDTO extends PlaylistWithoutOwnerDTO {
    private UserBasicInfoDTO user;
    private String playlistUrl;
}
