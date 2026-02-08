// ... שאר האימפורטים ...
package com.college.controllers;
import com.college.entities.BasicUser;
import com.college.entities.FollowRelationship;
import com.college.entities.Post;
import com.college.entities.PostLike;
import com.college.repositories.PostLikeRepository;
import com.college.repositories.PostRepository;
import com.college.repositories.UserRepository;
import com.college.repositories.FollowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class GeneralController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/register")
    public boolean register(@RequestBody BasicUser newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return false;
        }

        userRepository.save(newUser);
        return true;
    }


    @PostMapping("/login")
    public BasicUser login(@RequestBody BasicUser user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @GetMapping("/get-user-details")
    public BasicUser getUserDetails(@RequestParam String username) {
        return userRepository.findByUsername(username);
    }
    @PostMapping("/update-profile-pic")
    public boolean updateProfilePic(@RequestBody BasicUser userUpdate) {
        BasicUser existingUser = userRepository.findByUsername(userUpdate.getUsername());

        if (existingUser != null) {
            existingUser.setProfilePicUrl(userUpdate.getProfilePicUrl());
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    @GetMapping ("/search-users")
    public List<BasicUser> searchUsers(@RequestParam("query") String query){

        return userRepository.findByUsernameContaining(query);
    }

    @PostMapping("/follow")
    public boolean follow(@RequestParam String follower, @RequestParam String followed) {
        if (!followRepository.existsByFollowerUsernameAndFollowedUsername(follower, followed)) {
            FollowRelationship rel = new FollowRelationship();
            rel.setFollowerUsername(follower);
            rel.setFollowedUsername(followed);
            followRepository.save(rel);
            return true;
        }
        return false;
    }

    @Transactional
    @PostMapping("/unfollow")
    public void unfollow(@RequestParam String follower, @RequestParam String followed) {
        followRepository.deleteByFollowerUsernameAndFollowedUsername(follower, followed);
    }

    @GetMapping("/get-following")
    public List<FollowRelationship> getFollowing(@RequestParam String username) {
        return followRepository.findAllByFollowerUsername(username);
    }


    @GetMapping("/my-followers")
    public List<FollowRelationship> getFollowers(@RequestParam String username) {
        return followRepository.findAllByFollowedUsername(username);
    }


    @PostMapping("/add-post")
    public boolean addPost(@RequestBody Post post) {
        if (post.getContent() != null &&
                !post.getContent().trim().isEmpty() &&
                post.getContent().length() <= 500) {
            post.setCreatedAt(LocalDateTime.now());
            postRepository.save(post);
            return true;
        }
        return false;
    }

    @GetMapping("/get-posts")
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/get-following-feed")
    public List<Post> getFollowingFeed(@RequestParam String username) {
        Pageable topTwenty = PageRequest.of(0, 20);
        List<Post> posts = postRepository.findFollowingPosts(username, topTwenty);

        Collections.shuffle(posts);

        return posts;
    }

    @GetMapping("/get-post-count")
    public long getPostCount(@RequestParam String username) {
        return postRepository.countByUsername(username);
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<?> deletePost(@RequestParam Integer postId, @RequestParam String username) {
        return postRepository.findById(postId).map(post -> {
            // בדיקת אבטחה: רק מי שיצר את הפוסט יכול למחוק אותו
            if (post.getUsername().equals(username)) {
                postRepository.deleteById(postId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-user-posts")
    public List<Post> getUserPosts(@RequestParam("username") String username) {
        return postRepository.findByUsername(username);
    }

    @Transactional // חשוב עבור פעולות מחיקה
    @PostMapping("/toggle-like")
    public ResponseEntity<?> toggleLike(@RequestParam Integer postId, @RequestParam String username) {
        if (postLikeRepository.existsByPostIdAndUsername(postId, username)) {
            postLikeRepository.deleteByPostIdAndUsername(postId, username);
            return ResponseEntity.ok("Unliked");
        } else {
            PostLike like = new PostLike();
            like.setPostId(postId);
            like.setUsername(username);
            postLikeRepository.save(like);
            return ResponseEntity.ok("Liked");
        }
    }

    @GetMapping("/get-likes")
    public long getLikes(@RequestParam Integer postId) {
        return postLikeRepository.countByPostId(postId);
    }
    @GetMapping("/has-liked")
    public boolean hasLiked(@RequestParam Integer postId, @RequestParam String username) {
        return postLikeRepository.existsByPostIdAndUsername(postId, username);
    }

}
