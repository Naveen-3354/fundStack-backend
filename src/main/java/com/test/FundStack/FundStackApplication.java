package com.test.FundStack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.test.FundStack.configuration.AmfiProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAsync
@EnableConfigurationProperties(AmfiProperties.class)
@SpringBootApplication
public class FundStackApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundStackApplication.class, args);
	}

}
