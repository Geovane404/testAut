package com.gtecnologia.testAut.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.gtecnologia.testAut.dto.ProductDTO;
import com.gtecnologia.testAut.factory.Factory;
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
	
	private ProductDTO productDTO;

	// FIXTURES
	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProduct = 25L;
		
		productDTO = Factory.createProductDTO();
	}

	// ---TESTES PARA VALIDAR BUSCAS:
	@Test
	public void findALLPagedShouldReturnSortedPagewhenSortByName() {

		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

		Page<ProductDTO> result = service.findALLPaged(pageRequest);

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}

	@Test
	public void findALLPagedShouldReturnPagewhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findALLPaged(pageRequest);

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(countTotalProduct, result.getTotalElements());
	}

	@Test
	public void findALLPagedShouldReturnEmptyPagewhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(100, 10);

		Page<ProductDTO> result = service.findALLPaged(pageRequest);

		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingId, result.getId());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNoExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
	}
	
	
	// ---TESTES PARA VALIDAR INSERÇÕES E ATUALIZAÇÕES:
	@Test
	public void insertShouldReturnProductDTO() {
		
		ProductDTO dto = service.insert(productDTO);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(productDTO.getName(), dto.getName());
	}	
	
	@Test
	public void updateShoulReturnProductDTOWhenIdExist () {
		
		ProductDTO dto = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(productDTO.getId(), dto.getId());
		Assertions.assertEquals(productDTO.getName(), dto.getName());
	}	
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdNoExis() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
	}	
	
	
	// ---TESTES PARA VALIDAR DELEÇÕES:
	@Test
	public void deleteShouldDeleteResourceWhenIdExist() {

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
