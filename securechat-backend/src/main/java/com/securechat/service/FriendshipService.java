package com.securechat.service;

import com.securechat.model.Friendship;
import com.securechat.model.FriendshipStatus;

import java.util.List;

public interface FriendshipService {

    Friendship sendRequest(String addressee);

    Friendship updateStatus(Long friendshipId, FriendshipStatus status);

    List<Friendship> listMine();

    List<Friendship> listAccepted();
}
