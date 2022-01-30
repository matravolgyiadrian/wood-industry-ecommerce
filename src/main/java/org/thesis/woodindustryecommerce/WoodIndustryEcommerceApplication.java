package org.thesis.woodindustryecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@ComponentScan(basePackages = {"org.thesis.woodindustryecommerce"})
public class WoodIndustryEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoodIndustryEcommerceApplication.class, args);
	}

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		loggingFilter.setIncludeHeaders(false);
		return loggingFilter;
	}

}
