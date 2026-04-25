package com.securechat.dto;

import com.securechat.model.FriendshipStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private FriendshipStatus status;
}
