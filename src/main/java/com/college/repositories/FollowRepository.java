package com.college.repositories;

import com.college.entities.FollowRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowRelationship, Integer> {
    void deleteByFollowerUsernameAndFollowedUsername(String follower, String followed);
    boolean existsByFollowerUsernameAndFollowedUsername(String follower, String followed);
    List<FollowRelationship> findAllByFollowerUsername(String followerUsername);

    List<FollowRelationship> findAllByFollowedUsername(String followedUsername);
}
