package org.thesis.woodindustryecommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.repository.CouponRepository;
import org.thesis.woodindustryecommerce.services.implementations.CouponServiceImpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CouponServiceTest {

    private CouponServiceImpl underTest;

    private CouponRepository couponRepository;

    @BeforeEach
    void init(){
        couponRepository = Mockito.mock(CouponRepository.class);

        underTest = new CouponServiceImpl(couponRepository);
    }

    @Test
    void testFindAllShouldCallCouponRepository() {
        //Given

        //When
        underTest.findAll();

        //Then
        Mockito.verify(couponRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByIdShouldCallCouponRepository() {
        //Given
        Mockito.when(couponRepository.findById(0L)).thenReturn(Optional.of(new Coupon()));

        //When
        underTest.findById(0L);

        //Then
        Mockito.verify(couponRepository, Mockito.times(1)).findById(0L);
    }

    @Test
    void testFindByIdShouldThrowNoSuchElementException(){
        //Given
        Mockito.when(couponRepository.findById(0L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(NoSuchElementException.class, () -> underTest.findById(0L));

        //Then
    }

    @Test
    void testFindByCouponCodeShouldCallCouponRepository() {
        //Given

        //When
        underTest.findByCouponCode("code");

        //Then
        Mockito.verify(couponRepository, Mockito.times(1)).findByCouponCode("CODE");
    }

    @Test
    void testSaveShouldCallCouponRepository() {
        //Given

        //When
        underTest.save(Coupon.builder()
                .couponCode("CODE")
                .discountAmount(30)
                .build());

        //Then
        Mockito.verify(couponRepository, Mockito.times(1)).save(Coupon.builder()
                .couponCode("CODE")
                .discountAmount(30)
                .build());
    }

    @Test
    void testDeleteShouldCallCouponRepository() {
        //Given

        //When
        underTest.delete(0L);

        //Then
        Mockito.verify(couponRepository, Mockito.times(1)).deleteById(0L);
    }

    @Test
    void testValidateShouldReturnCouponWithZeroDiscount_WhenCodeDoesntExists() {
        //Given
        Mockito.when(couponRepository.findByCouponCode("CODE")).thenReturn(null);

        //When
        Coupon retrievedCoupon = underTest.validate("CODE");

        //Then
        Assertions.assertEquals(Coupon.builder()
                .discountAmount(0).build(), retrievedCoupon);
    }

    @Test
    void testValidateShouldReturnCoupon(){
        //Given
        Coupon coupon = Coupon.builder().couponCode("CODE").discountAmount(30).build();
        Mockito.when(couponRepository.findByCouponCode("CODE")).thenReturn(coupon);

        //When
        Coupon retrievedCoupon = underTest.validate("CODE");

        //Then
        Assertions.assertEquals(coupon, retrievedCoupon);
    }
}