package com.securechat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipRequest {

    @NotBlank(message = "Addressee is required")
    private String addressee;
}
