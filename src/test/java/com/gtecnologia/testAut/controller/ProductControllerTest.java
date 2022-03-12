package com.gtecnologia.testAut.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.gtecnologia.testAut.controllers.ProductController;
import com.gtecnologia.testAut.dto.ProductDTO;
import com.gtecnologia.testAut.entities.Product;
import com.gtecnologia.testAut.factory.Factory;
import com.gtecnologia.testAut.services.ProductService;
import com.gtecnologia.testAut.services.exceptions.ResourceNotFoundException;

//TESTES || UNITARIO COM MOCKITO - MOCKBEAN || VALIDAR METODOS DA MINHA CONTROLLER:

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	private long existingId;
	private long nonExistId;
	
	private Product product;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	
	// FIXTURES
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistId = 2L;
		
		product = Factory.createProduct();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		
		//RETURN => quando(when)--ação(thenReturn)
		Mockito.when(service.findALLPaged(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistId)).thenThrow(ResourceNotFoundException.class);
		
	}
	
	//---TESTE PARA VALIDAR BUSCAS:
	@Test
	public void findAllShouldReturnPage()throws Exception{
		
		ResultActions result = mockMvc.perform(get("/products")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test 
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());

	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	

}
