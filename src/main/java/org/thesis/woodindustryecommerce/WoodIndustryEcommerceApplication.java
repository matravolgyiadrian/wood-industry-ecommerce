package org.thesis.woodindustryecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
//@EnableJdbcHttpSession
@ComponentScan(basePackages = {"org.thesis.woodindustryecommerce"})
public class WoodIndustryEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoodIndustryEcommerceApplication.class, args);
	}

}
