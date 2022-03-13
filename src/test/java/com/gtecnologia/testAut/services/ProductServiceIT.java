package com.gtecnologia.testAut.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gtecnologia.testAut.repositories.ProductRepository;
import com.gtecnologia.testAut.services.exceptions.ResourceNotFoundException;


//TESTES || INTEGRAÇÃO || VALIDAR INTEGRAÇÃO DE COMPONENTES || SERVICE <---> REPOSITOTY


@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProduct;
	
	
	//FIXTURES
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProduct = 25L;
		
	}
	
	// ---TESTES PARA VALIDAR DELEÇÕES:
	@Test
	public void deleteShouldDeleteResourceWhenIdExist () {
		
		 service.delete(existingId);
		 Assertions.assertEquals(countTotalProduct - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
}

