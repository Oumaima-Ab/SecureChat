package com.securechat.repository;

import com.securechat.model.Friendship;
import com.securechat.model.FriendshipStatus;
import com.securechat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByRequesterAndAddressee(User requester, User addressee);

    Optional<Friendship> findByRequesterAndAddresseeOrRequesterAndAddressee(
            User requester,
            User addressee,
            User reverseRequester,
            User reverseAddressee
    );

    List<Friendship> findByRequesterOrAddresseeOrderByUpdatedAtDesc(User requester, User addressee);

    List<Friendship> findByStatusAndRequesterOrStatusAndAddresseeOrderByUpdatedAtDesc(
            FriendshipStatus requesterStatus,
            User requester,
            FriendshipStatus addresseeStatus,
            User addressee
    );
}
