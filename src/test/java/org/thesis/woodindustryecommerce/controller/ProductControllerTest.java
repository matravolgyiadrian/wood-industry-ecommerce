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
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.services.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
@OverrideAutoConfiguration(enabled = true)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetAllShouldAddAttributesProductsAndProductForm() throws Exception{
        //Given
        List<Product> products = List.of(Product.builder().name("Chair").price(100).stock(100).build());
        Mockito.when(productService.findAll()).thenReturn(products);

        //When
        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("product"))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("productForm", new Product()));

        //Then
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetNewProductShouldAddAttributes() throws Exception{
        //Given
        List<Product> products = List.of(Product.builder().name("Chair").price(100).stock(100).build());
        Mockito.when(productService.findAll()).thenReturn(products);

        //When
        mockMvc.perform(get("/product/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product"))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("productForm", new Product()))
                .andExpect(model().attribute("form", true))
                .andExpect(model().attribute("method", "new"));

        //Then
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testPostNewProductShouldSaveNewProduct() throws Exception {
        //Given

        //When
        mockMvc.perform(post("/product/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("name", "Chair"),
                                new BasicNameValuePair("price", "100"),
                                new BasicNameValuePair("stock", "100")
                        )))))
                .andExpect(redirectedUrl("/product/all"))
                .andExpect(view().name("redirect:/product/all"));

        //Then
        Mockito.verify(productService, Mockito.times(1))
                .save(Product.builder().name("Chair").stock(100).price(100).build());
    }



    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetEditProductShouldAddAttributes() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<Product> products = List.of(chair);
        Mockito.when(productService.findAll()).thenReturn(products);
        Mockito.when(productService.findById(1L)).thenReturn(chair);

        //When
        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product"))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("productForm", chair))
                .andExpect(model().attribute("form", true))
                .andExpect(model().attribute("method", "edit"));

        //Then
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testPostEditProductShouldEditProduct() throws Exception {
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");

        //When
        mockMvc.perform(post("/product/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("name", "Chair"),
                                new BasicNameValuePair("price", "100"),
                                new BasicNameValuePair("stock", "200")
                        )))))
                .andExpect(redirectedUrl("/product/all"))
                .andExpect(view().name("redirect:/product/all"));

        //Then
        chair.setStock(200);
        Mockito.verify(productService, Mockito.times(1))
                .save(chair);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testDeleteProductShouldCallDelete() throws Exception{
        //Given

        //When
        mockMvc.perform(post("/product/delete/1"))
                .andExpect(redirectedUrl("/product/all"))
                .andExpect(view().name("redirect:/product/all"));

        //Then
        Mockito.verify(productService, Mockito.times(1)).delete(1L);
    }
}