package com.gtecnologia.testAut.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gtecnologia.testAut.dto.ProductDTO;
import com.gtecnologia.testAut.repositories.ProductRepository;

//TESTES || UNITARIO COM MOCKITO || VALIDAR METODOS DO MEU COMPONENTE SERVICE:

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@MockBean
	private ProductRepository repo;
	
			
}

