package com.securechat.service.impl;

import com.securechat.model.Friendship;
import com.securechat.model.FriendshipStatus;
import com.securechat.model.User;
import com.securechat.repository.FriendshipRepository;
import com.securechat.repository.UserRepository;
import com.securechat.service.FriendshipService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Friendship sendRequest(String addresseeValue) {
        User requester = getAuthenticatedUser();
        User addressee = findUser(addresseeValue);

        if (requester.getId().equals(addressee.getId())) {
            throw new RuntimeException("You cannot send a friend request to yourself");
        }

        friendshipRepository.findByRequesterAndAddresseeOrRequesterAndAddressee(
                requester, addressee, addressee, requester
        ).ifPresent(existing -> {
            throw new RuntimeException("Friendship already exists");
        });

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setAddressee(addressee);
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship updateStatus(Long friendshipId, FriendshipStatus status) {
        User currentUser = getAuthenticatedUser();

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        boolean isRequester = friendship.getRequester().getId().equals(currentUser.getId());
        boolean isAddressee = friendship.getAddressee().getId().equals(currentUser.getId());

        if (!isRequester && !isAddressee) {
            throw new RuntimeException("You are not allowed to update this friendship");
        }

        if (!isAddressee && (status == FriendshipStatus.ACCEPTED || status == FriendshipStatus.REJECTED)) {
            throw new RuntimeException("Only addressee can accept or reject request");
        }

        friendship.setStatus(status);
        return friendshipRepository.save(friendship);
    }

    @Override
    public List<Friendship> listMine() {
        User currentUser = getAuthenticatedUser();
        return friendshipRepository.findByRequesterOrAddresseeOrderByUpdatedAtDesc(currentUser, currentUser);
    }

    @Override
    public List<Friendship> listAccepted() {
        User currentUser = getAuthenticatedUser();
        return friendshipRepository.findByStatusAndRequesterOrStatusAndAddresseeOrderByUpdatedAtDesc(
                FriendshipStatus.ACCEPTED,
                currentUser,
                FriendshipStatus.ACCEPTED,
                currentUser
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new RuntimeException("Unauthorized");
        }

        String currentUserEmail = authentication.getName();

        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private User findUser(String value) {
        return userRepository.findByEmail(value)
                .or(() -> userRepository.findByUsername(value))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
