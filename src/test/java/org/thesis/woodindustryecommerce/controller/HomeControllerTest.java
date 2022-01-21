package org.thesis.woodindustryecommerce.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.services.ProductService;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HomeController.class)
@ActiveProfiles("test")
@OverrideAutoConfiguration(enabled = true)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testHomeShouldAddAttributeProducts() throws Exception{
        //Given
        Product chair = Product.builder()
                .name("Chair")
                .price(100)
                .stock(100)
                .build();
        Mockito.when(productService.findAll()).thenReturn(List.of(chair));

        //When
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("products", List.of(chair)))
                .andExpect(model().attribute("shopping_cart", new LinkedList<>()));

        //Then

    }

    @Test
    void testAboutShouldReturnAboutUsView() throws Exception{
        //Given

        //When
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about_us"));

        //Then
    }

}