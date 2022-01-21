package org.thesis.woodindustryecommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.repository.ProductRepository;
import org.thesis.woodindustryecommerce.services.implementations.CloudinaryService;
import org.thesis.woodindustryecommerce.services.implementations.ProductServiceImpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductServiceTest {

    private ProductServiceImpl underTest;

    private ProductRepository productRepository;
    private CloudinaryService cloudinaryService;

    @BeforeEach
    public void init(){
        productRepository = Mockito.mock(ProductRepository.class);
        cloudinaryService = Mockito.mock(CloudinaryService.class);

        underTest = new ProductServiceImpl(cloudinaryService, productRepository);
    }

    @Test
    void testFindAllShouldCallProductRepository(){
        //Given

        //When
        underTest.findAll();

        //Then
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByIdShouldThrowNoSuchElementException() {
        //Given
        Mockito.when(productRepository.findById(10L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(NoSuchElementException.class, () ->
                underTest.findById(10L));
        //Then

    }

    @Test
    void testFindByIdShouldReturnProduct() {
        //Given
        Product chair = Product.builder().name("Chair").price(10000).stock(200).build();
        Mockito.when(productRepository.findById(10L)).thenReturn(Optional.ofNullable(chair));

        //When
        Product retrievedProduct = underTest.findById(10L);

        //Then
        Assertions.assertEquals(chair, retrievedProduct);
    }

    @Test
    void testSaveShouldReturnSavedProduct(){
        //Given
        Mockito.when(cloudinaryService.uploadFile(Mockito.any(MultipartFile.class))).thenReturn("url-to-image");
        Mockito.when(cloudinaryService.uploadFile(null)).thenReturn("");
        Mockito.when(productRepository.save(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);

        //When
        Product productOne = underTest.save(Product.builder()
                .name("Chair")
                .price(10000)
                .stock(100)
                .image(new MockMultipartFile("foo", "foo".getBytes()))
                .build());
        Product productTwo = underTest.save(Product.builder()
                .name("Chair")
                .price(10000)
                .stock(100)
                .build());

        //Then
        Product expectedProductOne = productOne;
        expectedProductOne.setImageUrl("url-to-image");
        Assertions.assertEquals(expectedProductOne, productOne);

        Assertions.assertEquals(Product.builder()
                .name("Chair")
                .price(10000)
                .stock(100)
                .build(), productTwo);
    }

    @Test
    void testDeleteShouldCallProductRepository(){
        //Given

        //When
        underTest.delete(0L);

        //Then
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(0L);
    }
}