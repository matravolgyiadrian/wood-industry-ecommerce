package org.thesis.woodindustryecommerce.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Role;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.RoleRepository;
import org.thesis.woodindustryecommerce.repository.UserRepository;
import org.thesis.woodindustryecommerce.services.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User createUser(User user, String userRoleStr) {
        User localUser = userRepository.findByUsername(user.getUsername());

        Role role = findAuthority(userRoleStr);

        if(localUser != null){
            log.info("User {} already exists. Nothing will be done!", user.getUsername());
        } else {
            user.setAuthorities(Set.of(role));

            localUser = this.save(user);
        }

        return localUser;
    }

    @Override
    public User createGuestUser(String name, String email, String address) {
        User user = User.builder()
                .username("guest_"+userRepository.count())
                .password("")
                .fullName(name)
                .email(email)
                .address(address)
                .build();

        return this.save(user);
    }

    @Override
    public User editUser(User user) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if(!user.getFullName().isEmpty() && !user.getFullName().isBlank()){
            localUser.setFullName(user.getFullName());
        }
        if(!user.getEmail().isEmpty() && !user.getEmail().isBlank()){
            localUser.setEmail(user.getEmail());
        }
        if(!user.getAddress().isEmpty() && !user.getAddress().isBlank()){
            localUser.setAddress(user.getAddress());
        }
        if(!user.getPassword().isEmpty() && !user.getPassword().isBlank()){
            localUser.setPassword(user.getPassword());
        }

        this.save(localUser);

        return localUser;
    }

    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private Role findAuthority(String userRoleStr) {
        switch (userRoleStr) {
            case "ROLE_USER":
                return roleRepository.findByAuthority("ROLE_USER");
            case "ROLE_ADMIN":
                return roleRepository.findByAuthority("ROLE_ADMIN");
            default:
                throw new IllegalArgumentException("There is no authority like this");
        }
    }
}
