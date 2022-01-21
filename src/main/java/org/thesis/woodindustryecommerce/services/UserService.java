package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.User;

import java.util.List;

@Service
public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    User createUser(User user, String userRoleStr);
    User createGuestUser(String name, String email, String address);
    User editUser(User user);
    User save(User user);
}
