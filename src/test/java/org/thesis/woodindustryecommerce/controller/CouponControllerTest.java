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
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.services.CouponService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CouponController.class)
@ActiveProfiles("test")
@OverrideAutoConfiguration(enabled = true)
@WithMockUser(authorities = "ROLE_ADMIN")
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Test
    void testGetCouponsShouldAddAttributeWithAllCoupons() throws Exception{
        //Given
        Coupon coupon = Coupon.builder()
                .couponCode("CODE20")
                .discountAmount(20)
                .build();
        Mockito.when(couponService.findAll()).thenReturn(List.of(coupon));

        //When
        mockMvc.perform(get("/coupon/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("coupon"))
                .andExpect(model().attribute("coupons", List.of(coupon)));

        //Then
    }

    @Test
    void testRenderNewCouponShouldAddAttributes() throws Exception{
        //Given
        Coupon coupon = Coupon.builder()
                .couponCode("CODE20")
                .discountAmount(20)
                .build();
        Mockito.when(couponService.findAll()).thenReturn(List.of(coupon));

        //When
        mockMvc.perform(get("/coupon/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("coupon"))
                .andExpect(model().attribute("coupons", List.of(coupon)))
                .andExpect(model().attribute("form", true))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("couponToEdit", new Coupon()));

        //Then
    }

    @Test
    void testNewCouponShouldCreateNewCoupon() throws Exception{
        //Given
        Coupon coupon = Coupon.builder()
                .couponCode("CODE20")
                .discountAmount(20)
                .build();
        Mockito.when(couponService.findAll()).thenReturn(List.of());

        //When
        mockMvc.perform(post("/coupon/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("couponCode", "code20"),
                        new BasicNameValuePair("discountAmount", "20")
                )))))
                .andExpect(redirectedUrl("/coupon/all"))
                .andExpect(view().name("redirect:/coupon/all"));

        //Then
        Mockito.verify(couponService, Mockito.times(1)).save(coupon);
    }
    @Test
    void testNewCouponShouldAddAttributeCodeAlreadyExists() throws Exception{
        //Given
        Coupon coupon = Coupon.builder()
                .couponCode("CODE20")
                .discountAmount(20)
                .build();
        Mockito.when(couponService.findAll()).thenReturn(List.of(coupon));

        //When
        mockMvc.perform(post("/coupon/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("couponCode", "code20"),
                        new BasicNameValuePair("discountAmount", "20")
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("coupon"))
                .andExpect(model().attribute("codeAlreadyExists", true))
                .andExpect(model().attribute("coupons", List.of(coupon)))
                .andExpect(model().attribute("form", true))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("couponToEdit", coupon));

        //Then
    }

    @Test
    void testRenderEditCouponShouldAddAttributes() throws Exception{
        //Given
        Coupon coupon = new Coupon(1L, "CODE20", 20);
        Mockito.when(couponService.findAll()).thenReturn(List.of(coupon));
        Mockito.when(couponService.findById(1L)).thenReturn(coupon);

        //When
        mockMvc.perform(get("/coupon/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("coupon"))
                .andExpect(model().attribute("coupons", List.of(coupon)))
                .andExpect(model().attribute("form", true))
                .andExpect(model().attribute("isEdit", true))
                .andExpect(model().attribute("couponToEdit", coupon));

        //Then
    }

    @Test
    void testEditCouponShouldEditCoupon() throws Exception {
        //Given
        Coupon coupon = new Coupon(1L, "CODE20", 20);
        Mockito.when(couponService.findAll()).thenReturn(List.of(coupon));
        Mockito.when(couponService.findById(1L)).thenReturn(coupon);

        //When
        mockMvc.perform(post("/coupon/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("couponCode", "code20"),
                                new BasicNameValuePair("discountAmount", "20")
                        )))))
                .andExpect(redirectedUrl("/coupon/all"))
                .andExpect(view().name("redirect:/coupon/all"));

        //Then
        Mockito.verify(couponService, Mockito.times(1)).save(coupon);
    }

    @Test
    void testDeleteCouponShouldDeleteCoupon() throws Exception {
        //Given

        //When
        mockMvc.perform(post("/coupon/delete/1"))
                .andExpect(redirectedUrl("/coupon/all"))
                .andExpect(view().name("redirect:/coupon/all"));

        //Then
        Mockito.verify(couponService, Mockito.times(1)).delete(1L);
    }
}