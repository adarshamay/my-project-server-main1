package com.college.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class BasicUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column
    private String profilePicUrl;

    public BasicUser() {}

    public BasicUser(String username, String password,String profilePicUrl) {
        this.username = username;
        this.password = password;
        this.profilePicUrl=profilePicUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicUrl() {return profilePicUrl;}
    public void setProfilePicUrl(String profilePicUrl) {this.profilePicUrl = profilePicUrl;}
}