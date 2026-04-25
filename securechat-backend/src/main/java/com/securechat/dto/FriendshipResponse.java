package com.securechat.dto;

import com.securechat.model.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FriendshipResponse {
    private Long id;
    private String requester;
    private String addressee;
    private FriendshipStatus status;
    private LocalDateTime updatedAt;
}
