package com.college.repositories;

import com.college.entities.BasicUser;
import org.apache.catalina.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<BasicUser, Integer> {
    boolean existsByUsername(String username);
    List<BasicUser> findByUsernameContaining(String username);
    List<BasicUser> findByUsernameContainingIgnoreCase(String username);

    boolean existsByUsernameAndPassword(String username, String password);
    BasicUser findByUsername(String username);

    BasicUser findByUsernameAndPassword(String username, String password);


}
