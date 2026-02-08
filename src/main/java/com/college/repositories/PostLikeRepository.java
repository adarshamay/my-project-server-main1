package com.college.repositories;


import com.college.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    boolean existsByPostIdAndUsername(Integer postId, String username);

    void deleteByPostIdAndUsername(Integer postId, String username);

    long countByPostId(Integer postId);
}
