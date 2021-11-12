package org.thesis.woodindustryecommerce;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Role;

@SpringBootApplication
@ComponentScan(basePackages = {"org.thesis.woodindustryecommerce"})
public class WoodIndustryEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoodIndustryEcommerceApplication.class, args);
	}

}
