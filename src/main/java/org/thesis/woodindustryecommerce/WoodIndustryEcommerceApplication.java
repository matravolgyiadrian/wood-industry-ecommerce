package org.thesis.woodindustryecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.thesis.woodindustryecommerce"})
public class WoodIndustryEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoodIndustryEcommerceApplication.class, args);
	}

}
