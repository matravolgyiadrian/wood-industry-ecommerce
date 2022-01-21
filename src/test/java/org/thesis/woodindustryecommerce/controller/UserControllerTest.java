package org.thesis.woodindustryecommerce.controller;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.thesis.woodindustryecommerce.model.*;
import org.thesis.woodindustryecommerce.services.OrderService;
import org.thesis.woodindustryecommerce.services.UserService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@OverrideAutoConfiguration(enabled = true)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;

    @Test
    void testLoginShouldAddAttributeToLogin() throws Exception{
        //Given

        //When
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("method", "login"));

        //Then
    }

    @Test
    void testLoginShouldAddAttributeError() throws Exception{
        //Given

        //When
        mockMvc.perform(get("/login").param("error", "Some error"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Error"));

        //Then
    }

    @Test
    void testGetRegisterShouldAddNewUserAttribute() throws Exception{
        //Given

        //When
        mockMvc.perform(get("/register")
                        .sessionAttr("userForm", new User()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("userForm", new User()));

        //Then
    }

    @Test
    void testPostRegisterShouldCreateUser() throws Exception{
        //Given
        Mockito.when(userService.findByUsername(Mockito.anyString())).thenReturn(null);
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(null);

        User jondoe = User.builder()
                .username("jondoe")
                .fullName("Jon Doe")
                .password("password")
                .email("jondoe@email")
                .address("address").build();

        //When
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("username", "jondoe"),
                                new BasicNameValuePair("password", "password"),
                                new BasicNameValuePair("email", "jondoe@email"),
                                new BasicNameValuePair("address", "address")
                        )))))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        //Then
        Mockito.verify(userService, Mockito.times(1))
                .createUser(jondoe, "ROLE_USER");
    }

    @Test
    void testPostRegisterShouldAddAttributeUsernameExists() throws Exception{
        //Given
        Mockito.when(userService.findByUsername("jondoe")).thenReturn(new User());
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(null);

        //When
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("username", "jondoe"),
                                new BasicNameValuePair("password", "password"),
                                new BasicNameValuePair("email", "jondoe@email"),
                                new BasicNameValuePair("address", "address")
                        )))))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("usernameExists", true));

        //Then
        Mockito.verify(userService, Mockito.times(1))
                .findByUsername("jondoe");
    }

    @Test
    void testPostRegisterShouldAddAttributeEmailExists() throws Exception{
        //Given
        Mockito.when(userService.findByEmail("jondoe@email")).thenReturn(new User());
        Mockito.when(userService.findByUsername(Mockito.anyString())).thenReturn(null);

        //When
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("username", "jondoe"),
                                new BasicNameValuePair("password", "password"),
                                new BasicNameValuePair("email", "jondoe@email"),
                                new BasicNameValuePair("address", "address")
                        )))))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("emailExists", true));

        //Then
        Mockito.verify(userService, Mockito.times(1))
                .findByUsername("jondoe");
    }

    @Test
    @WithMockUser(username = "jondoe")
    void testUserDetailsShouldAddAttributes() throws Exception{
        //Given
        User jondoe = User.builder()
                .username("jondoe")
                .fullName("Jon Doe")
                .password("password")
                .email("jondoe@email")
                .address("address").build();
        Order order = Order.builder()
                .shippingAddress("address")
                .customer("jondoe")
                .status(Status.PENDING)
                .products(List.of(
                        new CartItem(Product.builder().name("Chair").price(100D).stock(100).build(),2)
                )).build();
        Mockito.when(userService.findByUsername("jondoe")).thenReturn(jondoe);
        Mockito.when(orderService.findByCustomer("jondoe")).thenReturn(List.of(order));

        //When
        mockMvc.perform(get("/user-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("myprofile"))
                .andExpect(model().attribute("user", jondoe))
                .andExpect(model().attribute("orders", List.of(order)));

        //Then
    }

    @Test
    @WithMockUser(username = "jondoe")
    void testGetEditUserShouldAddAttribute() throws Exception{
        //Given
        User jondoe = User.builder()
                .username("jondoe")
                .fullName("Jon Doe")
                .password("password")
                .email("jondoe@email")
                .address("address").build();
        Mockito.when(userService.findByUsername("jondoe")).thenReturn(jondoe);
        //When
        mockMvc.perform(get("/user/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_user"))
                .andExpect(model().attribute("userToEdit", jondoe));

        //Then

    }

    @Test
    @WithMockUser(username = "jondoe")
    void testPostEditUserShouldEditUser() throws Exception{
        //Given
        Mockito.when(userService.findByEmail("jondoe@email")).thenReturn(null);

        //When
        mockMvc.perform(post("/user/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("password", "password"),
                                new BasicNameValuePair("email", "jondoe@email"),
                                new BasicNameValuePair("address", "address")
                        )))))
                .andExpect(redirectedUrl("/user-details"))
                .andExpect(view().name("redirect:/user-details"));

        //Then
        User userToEdit = User.builder()
                .username("jondoe")
                .fullName("Jon Doe")
                .password("password")
                .email("jondoe@email")
                .address("address").build();
        Mockito.verify(userService, Mockito.times(1)).editUser(userToEdit);
    }

    @Test
    @WithMockUser(username = "jondoe")
    void testPostEditUserShouldAddAttributeEmailExists() throws Exception{
        //Given
        User jondoe = User.builder()
                .username("jondoe")
                .fullName("Jon Doe")
                .password("password")
                .email("jondoe@email")
                .address("address").build();
        User jonSnow = User.builder()
                .username("jonSnow")
                .fullName("Jon Snow")
                .password("password")
                .email("jon@email")
                .address("address").build();
        Mockito.when(userService.findByEmail("jon@email")).thenReturn(jonSnow);
        Mockito.when(userService.findByUsername("jondoe")).thenReturn(jondoe);

        //When
        mockMvc.perform(post("/user/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("password", "password"),
                                new BasicNameValuePair("email", "jon@email"),
                                new BasicNameValuePair("address", "address")
                        )))))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_user"))
                .andExpect(model().attribute("emailExists", true))
                .andExpect(model().attribute("userToEdit", jondoe));

        //Then
    }
}