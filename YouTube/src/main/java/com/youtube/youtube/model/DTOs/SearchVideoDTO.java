package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//public record SearchVideoDTO (int id, UserBasicInfoDTO owner, LocalDateTime dateCreated,
//                              String name, long views, String videoUrl) {
//}
@Getter
@Setter
@NoArgsConstructor

public class SearchVideoDTO {
    private int id;
    private UserBasicInfoDTO user;
    private LocalDateTime dateCreated;
    private  String name;
    private long views;
    private String videoUrl;
}