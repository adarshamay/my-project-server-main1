package com.college.repositories;

import com.college.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post>findAllByOrderByCreatedAtDesc();

    List<Post> findByUsername(String username);

    @Query("SELECT p FROM Post p WHERE p.username IN " +
            "(SELECT f.followedUsername FROM FollowRelationship f WHERE f.followerUsername = :username) " +
            "ORDER BY p.createdAt DESC")
    List<Post> findFollowingPosts(@Param("username") String username, Pageable pageable);
    long countByUsername(String username);
}
