package com.youtube.youtube.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserWatchVideo implements Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "video_id")
    private int videoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWatchVideo that = (UserWatchVideo) o;
        return userId == that.userId && videoId == that.videoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, videoId);
    }
}
