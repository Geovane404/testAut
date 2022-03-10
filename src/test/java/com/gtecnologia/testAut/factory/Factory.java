package com.gtecnologia.testAut.factory;

import java.time.Instant;

import com.gtecnologia.testAut.entities.Category;
import com.gtecnologia.testAut.entities.Product;

public class Factory {
	
	
	public static Product createProduct() {
		
		Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png", Instant.parse("2022-02-16T03:00:00Z"));
		product.getCategories().add(new Category(1L,"Electronics"));
		return product;
	}

	
}
