package com.securechat.controller;

import com.securechat.dto.FriendshipRequest;
import com.securechat.dto.FriendshipResponse;
import com.securechat.dto.FriendshipStatusUpdateRequest;
import com.securechat.model.Friendship;
import com.securechat.service.FriendshipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/request")
    public ResponseEntity<FriendshipResponse> sendRequest(@Valid @RequestBody FriendshipRequest request) {
        Friendship friendship = friendshipService.sendRequest(request.getAddressee());
        return ResponseEntity.ok(toResponse(friendship));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FriendshipResponse> updateStatus(@PathVariable Long id,
                                                           @Valid @RequestBody FriendshipStatusUpdateRequest request) {
        Friendship friendship = friendshipService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(toResponse(friendship));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<FriendshipResponse>> listMine() {
        return ResponseEntity.ok(friendshipService.listMine().stream().map(this::toResponse).toList());
    }

    @GetMapping("/accepted")
    public ResponseEntity<List<FriendshipResponse>> listAccepted() {
        return ResponseEntity.ok(friendshipService.listAccepted().stream().map(this::toResponse).toList());
    }

    private FriendshipResponse toResponse(Friendship friendship) {
        return new FriendshipResponse(
                friendship.getId(),
                friendship.getRequester().getUsername(),
                friendship.getAddressee().getUsername(),
                friendship.getStatus(),
                friendship.getUpdatedAt()
        );
    }
}
