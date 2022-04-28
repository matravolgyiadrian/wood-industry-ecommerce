package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(value="User.detail")
    User findByUsername(String username);
    @EntityGraph(value="User.detail")
    User findByEmail(String email);
}
