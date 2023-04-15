package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

//public record UserVideosDTO (int id, String firstName, String lastName, Set<VideoWithoutOwnerDTO> videos) {
//}
@Getter
@Setter
@NoArgsConstructor
public class UserVideosDTO {
    private int id;
    private String firstName;
    private String lastName;
    private Set<VideoWithoutOwnerDTO> videos;
}
