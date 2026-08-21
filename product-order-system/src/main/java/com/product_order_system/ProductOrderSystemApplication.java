package com.product_order_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductOrderSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductOrderSystemApplication.class, args);
        System.err.println("Application Started Successfully..!");
	}
}