package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.User;

import java.util.List;

@Service
public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void delete(Long id);
}
