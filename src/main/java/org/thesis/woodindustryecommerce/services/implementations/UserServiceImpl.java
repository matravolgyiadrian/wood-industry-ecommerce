package org.thesis.woodindustryecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.UserRepository;
import org.thesis.woodindustryecommerce.services.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
