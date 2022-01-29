package org.thesis.woodindustryecommerce.controller;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thesis.woodindustryecommerce.model.*;
import org.thesis.woodindustryecommerce.model.binding.Billing;
import org.thesis.woodindustryecommerce.services.CouponService;
import org.thesis.woodindustryecommerce.services.OrderService;
import org.thesis.woodindustryecommerce.services.ProductService;
import org.thesis.woodindustryecommerce.services.UserService;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CartController.class)
@ActiveProfiles("test")
@OverrideAutoConfiguration(enabled = true)
class CartControllerTest {

    private MockMvc mockMvc;

    private MockHttpSession session;

    @Autowired
    FilterChainProxy springSecurityFilterChain;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CouponService couponService;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new CartController(userService, productService, orderService, couponService))
                .apply(sharedHttpSession())
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
        session = new MockHttpSession();
    }

    @Test
    void testAddToCartShouldAddItemToCart() throws Exception {
        //Given
        Product chair = Product.builder().name("Chair").price(100).stock(100).build();
        Mockito.when(productService.findById(1L)).thenReturn(chair);
        List<CartItem> expectedCart = new LinkedList<>(List.of(new CartItem(chair, 1)));
        session.setAttribute("shopping_cart", new LinkedList<>());

        //When
        mockMvc.perform(post("/cart/add").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("id", "1"),
                        new BasicNameValuePair("quantity", "1")
                )))))
                .andExpect(redirectedUrl("/home"))
                .andExpect(view().name("redirect:/home"))
                .andExpect(request().sessionAttribute("shopping_cart", expectedCart))
                .andDo(print());

        //Then
    }
    @Test
    void testAddToCartShouldNotAddToCart_WhenThereIsNoOneInStock() throws Exception {
        //Given
        Product chair = Product.builder().name("Chair").price(100).stock(0).build();
        Mockito.when(productService.findById(1L)).thenReturn(chair);
        session.setAttribute("shopping_cart", new LinkedList<>());

        //When
        mockMvc.perform(post("/cart/add").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("id", "1"),
                        new BasicNameValuePair("quantity", "1")
                )))))
                .andExpect(redirectedUrl("/home"))
                .andExpect(view().name("redirect:/home"))
                .andExpect(flash().attribute("notEnoughInStock", true))
                .andExpect(request().sessionAttribute("shopping_cart", new LinkedList<>()))
                .andDo(print());

        //Then
    }

    @Test
    void testRemoveFromCartShouldRemoveItemFromCart() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<CartItem> cart = new LinkedList<>(List.of(new CartItem(chair, 1)));
        session.setAttribute("shopping_cart", cart);

        //When
        mockMvc.perform(post("/cart/remove").session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(List.of(
                                new BasicNameValuePair("id", "1")
                        )))))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(request().sessionAttribute("shopping_cart", new LinkedList<>()))
                .andDo(print());

        //Then
    }

    @Test
    void testCartDetailsShouldRenderCartDetails() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<CartItem> cart = new LinkedList<>(List.of(new CartItem(chair, 2)));
        session.setAttribute("shopping_cart", cart);

        //When
        mockMvc.perform(get("/cart/details").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("total_price", 200.0))
                .andExpect(model().attribute("shopping_cart", cart));

        //THen
    }

    @Test
    void testGETCheckoutShouldRenderCheckoutPageWithNewUserInModelAttribute() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<CartItem> cart = new LinkedList<>(List.of(new CartItem(chair, 2)));
        session.setAttribute("shopping_cart", cart);

        //When
        mockMvc.perform(get("/cart/checkout").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout_page"))
                .andExpect(model().attribute("total_price", 200.0))
                .andExpect(model().attribute("billingForm", new Billing()));

        //Then
    }

    @Test
    @WithMockUser(username = "jondoe")
    void testGETCheckoutShouldRenderCheckoutPageWithLoggedInUserInModelAttribute() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<CartItem> cart = new LinkedList<>(List.of(new CartItem(chair, 2)));
        session.setAttribute("shopping_cart", cart);
        User jondoe = User.builder()
                .username("jondoe")
                .email("jondoe@email")
                .address("address")
                .fullName("Jon Doe")
                .build();
        Mockito.when(userService.findByUsername(Mockito.anyString())).thenReturn(jondoe);

        //When
        mockMvc.perform(get("/cart/checkout").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout_page"))
                .andExpect(model().attribute("total_price", 200.0))
                .andExpect(model().attribute("billingForm", new Billing("Jon Doe", "jondoe@email", "address", null, null, null, null, null)));

        //Then
    }

    @Test
    void testGETCheckoutShouldReturnCartWithEmptyCartModelAttribute() throws Exception{
        //Given
        session.setAttribute("shopping_cart", new LinkedList<>());

        //When
        mockMvc.perform(get("/cart/checkout").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("total_price", 0.0))
                .andExpect(model().attribute("emptyCart", true));

        //Then
    }

    @Test
    @WithMockUser(username = "jondoe")
    void testPOSTCheckoutShouldProcessCheckoutAndSaveOrder() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<CartItem> cart = new LinkedList<>(List.of(new CartItem(chair, 2)));
        session.setAttribute("shopping_cart", cart);
        Order order = Order.builder()
                .customer("Jon Doe")
                .email("jondoe@email")
                .totalPrice(200.0)
                .products(cart)
                .shippingAddress("address")
                .build();

        //When
        mockMvc.perform(post("/cart/checkout").session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("discountMultiplier", "1"),
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("email", "jondoe@email"),
                                new BasicNameValuePair("address", "address"),
                                new BasicNameValuePair("paymentMethod", "debit"),
                                new BasicNameValuePair("cc-name", "Jon Doe"),
                                new BasicNameValuePair("cc-number", "123456789"),
                                new BasicNameValuePair("cc-expiration", "01/24"),
                                new BasicNameValuePair("cc-cvv", "123")

                        )))))
                .andExpect(redirectedUrl("/home?total_price=200.0"))
                .andExpect(view().name("redirect:/home"))
                .andExpect(request().sessionAttribute("shopping_cart", new LinkedList<>()));

        //Then
        chair.setStock(98);
        Mockito.verify(productService, Mockito.times(1)).save(chair);
        Mockito.verify(orderService, Mockito.times(1)).save(order);
    }

    @Test
    void testPOSTCheckoutShouldProcessCheckoutAndSaveOrder_WithGuest() throws Exception{
        //Given
        Product chair = new Product(1L, "Chair", 100, 100, null, "");
        List<CartItem> cart = new LinkedList<>(List.of(new CartItem(chair, 2)));
        session.setAttribute("shopping_cart", cart);
        Order order = Order.builder()
                .customer("Jon Doe /GUEST/")
                .email("jondoe@email")
                .totalPrice(200.0)
                .products(cart)
                .shippingAddress("address")
                .build();

        //When
        mockMvc.perform(post("/cart/checkout").session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("discountMultiplier", "1"),
                                new BasicNameValuePair("fullName", "Jon Doe"),
                                new BasicNameValuePair("email", "jondoe@email"),
                                new BasicNameValuePair("address", "address"),
                                new BasicNameValuePair("paymentMethod", "debit"),
                                new BasicNameValuePair("cc-name", "Jon Doe"),
                                new BasicNameValuePair("cc-number", "123456789"),
                                new BasicNameValuePair("cc-expiration", "01/24"),
                                new BasicNameValuePair("cc-cvv", "123")

                        )))))
                .andExpect(redirectedUrl("/home?total_price=200.0"))
                .andExpect(view().name("redirect:/home"))
                .andExpect(request().sessionAttribute("shopping_cart", new LinkedList<>()));

        //Then
        chair.setStock(98);
        Mockito.verify(productService, Mockito.times(1)).save(chair);
        Mockito.verify(orderService, Mockito.times(1)).save(order);
    }
}