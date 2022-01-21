package org.thesis.woodindustryecommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.thesis.woodindustryecommerce.model.Role;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.RoleRepository;
import org.thesis.woodindustryecommerce.repository.UserRepository;
import org.thesis.woodindustryecommerce.services.implementations.UserServiceImpl;

import java.util.Set;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceTest {

    private UserServiceImpl underTest;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);

        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        underTest = new UserServiceImpl(userRepository, roleRepository);
    }

    @Test
    void testFindByUsernameShouldReturnUser() {
        //Given
        User jonDoe = User.builder().username("jondoe").fullName("Jon Doe").authorities(Set.of(new Role(0L, "ROLE_USER"))).email("jondoe@email").address("address").build();
        Mockito.when(userRepository.findByUsername("jondoe")).thenReturn(jonDoe);

        //When
        User retrievedUser = underTest.findByUsername("jondoe");

        //Then
        Assertions.assertEquals(jonDoe, retrievedUser);
    }
    @Test
    void testFindByUsernameShouldReturnNull_WhenUsernameDoesntExists() {
        //Given
        Mockito.when(userRepository.findByUsername("jondoe")).thenReturn(null);

        //When
        User retrievedUser = underTest.findByUsername("jondoe");

        //Then
        Assertions.assertNull(retrievedUser);
    }

    @Test
    void testFindByEmailShouldReturnUser() {
        //Given
        User jonDoe = User.builder().username("jondoe").fullName("Jon Doe").authorities(Set.of(new Role(0L, "ROLE_USER"))).email("jondoe@email").address("address").build();
        Mockito.when(userRepository.findByEmail("jondoe@email")).thenReturn(jonDoe);

        //When
        User retrievedUser = underTest.findByEmail("jondoe@email");

        //Then
        Assertions.assertEquals(jonDoe, retrievedUser);
    }

    @Test
    void testFindByEmailShouldReturnNull_WhenEmailDoesntExists() {
        //Given
        Mockito.when(userRepository.findByEmail("jondoe@email")).thenReturn(null);

        //When
        User retrievedUser = underTest.findByEmail("jondoe@email");

        //Then
        Assertions.assertNull(retrievedUser);
    }

    @Test
    void testCreateUserShouldReturnExistingUser_WhenThereIsOne() {
        //Given
        User jonDoe = User.builder().username("jondoe").fullName("Jon Doe").authorities(Set.of(new Role(0L, "ROLE_USER"))).email("jondoe@email").address("address").build();
        Mockito.when(userRepository.findByUsername("jondoe")).thenReturn(jonDoe);

        //When
        User newUser = underTest.createUser(jonDoe, "ROLE_USER");

        //Then
        Assertions.assertEquals(jonDoe, newUser);
    }
    @Test

    void testCreateUserShouldReturnNewUser_WhenThereIsntOneYet() {
        //Given
        Role user = new Role(1L, "ROLE_USER");
        User jonDoe = User.builder().username("jondoe").password("password123").fullName("Jon Doe").email("jondoe@email").address("address").build();
        Mockito.when(userRepository.findByUsername("jondoe")).thenReturn(null);
        Mockito.when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(user);
        User newJonDoe = jonDoe;
        newJonDoe.setAuthorities(Set.of(user));
        newJonDoe.setPassword(bCryptPasswordEncoder.encode(jonDoe.getPassword()));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(newJonDoe);

        //When
        User newUser = underTest.createUser(jonDoe, "ROLE_USER");

        //Then
        Assertions.assertEquals(newJonDoe, newUser);
    }

    @Test
    void testCreateGuestUserShouldReturnUser() {
        //Given
        User jonDoe = User.builder().username("quest_1").password("").fullName("Jon Doe").email("jondoe@email").address("address").build();
        Mockito.when(userRepository.count()).thenReturn(1L);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(jonDoe);

        //When
        User newUser = underTest.createGuestUser("Jon Doe", "jondoe@email", "address");

        //Then
        Assertions.assertEquals(jonDoe, newUser);
    }

    @Test
    void testEditUserShouldReturnEditedUser() {
        //Given
        User jonDoe = User.builder().username("jondoe").password("password").fullName("Jon Doe").authorities(Set.of(new Role(0L, "ROLE_USER"))).email("jondoe@email").address("address").build();
        Mockito.when(userRepository.findByUsername("jondoe")).thenReturn(jonDoe);
        
        //When
        User fullNameChangedUser = User.builder().username("jondoe").password("").fullName("Jhon Doe").email("").address("").build();
        User editedFullNameUser = underTest.editUser(fullNameChangedUser);

        User emailChangedUser = User.builder().username("jondoe").password("").fullName("").email("jondoe1@email").address("").build();
        User editedEmailUser = underTest.editUser(emailChangedUser);

        User addressChangedUser = User.builder().username("jondoe").password("").fullName("").email("").address("address 123").build();
        User editedAddressUser = underTest.editUser(addressChangedUser);

        User passwordChangedUser = User.builder().username("jondoe").password("password123").fullName("").email("").address("").build();
        User editedPasswordUser = underTest.editUser(passwordChangedUser);

        User changedUser = User.builder().username("jondoe").password("password123").fullName("Jhon Doe").email("johndoe1@email").address("address123").build();
        User editedUser = underTest.editUser(changedUser);
        
        //Then
        User fullNameJonDoe = jonDoe;
        fullNameJonDoe.setFullName("Jhon Doe");
        Assertions.assertEquals(fullNameJonDoe, editedFullNameUser);

        User emailJonDoe = jonDoe;
        emailJonDoe.setEmail("jondoe1@email");
        Assertions.assertEquals(emailJonDoe, editedEmailUser);

        User addressJonDoe = jonDoe;
        addressJonDoe.setAddress("adress 123");
        Assertions.assertEquals(addressJonDoe, editedAddressUser);

        User passwordJonDoe = jonDoe;
        passwordJonDoe.setPassword(bCryptPasswordEncoder.encode("password123"));
        Assertions.assertEquals(passwordJonDoe, editedPasswordUser);

        User editedJonDoe = jonDoe;
        editedJonDoe.setFullName("Jhon Doe");
        editedJonDoe.setEmail("jondoe1@email");
        editedJonDoe.setAddress("adress 123");
        editedJonDoe.setPassword(bCryptPasswordEncoder.encode("password123"));
        Assertions.assertEquals(editedJonDoe, editedJonDoe);
    }

    @Test
    void testSaveShouldReturnSavedUser() {
        //Given
        User jonDoe = User.builder().username("jondoe").password("password").fullName("Jon Doe").authorities(Set.of(new Role(0L, "ROLE_USER"))).email("jondoe@email").address("address").build();
        User expectedJonDoe = jonDoe;
        expectedJonDoe.setPassword(bCryptPasswordEncoder.encode(jonDoe.getPassword()));
        Mockito.when(userRepository.save(jonDoe)).thenReturn(expectedJonDoe);

        //When
        User savedUser = underTest.save(jonDoe);

        //Then
//        Assertions.assertThat(savedUser).isEqualTo(jonDoe);
    }

}