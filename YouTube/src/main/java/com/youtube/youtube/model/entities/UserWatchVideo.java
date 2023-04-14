package com.youtube.youtube.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

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
