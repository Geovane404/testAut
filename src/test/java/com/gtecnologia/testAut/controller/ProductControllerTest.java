package com.gtecnologia.testAut.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.gtecnologia.testAut.factory.Factory;
import com.gtecnologia.testAut.services.ProductService;

//TESTES || UNITARIO COM MOCKITO - MOCKBEAN || VALIDAR METODOS DA MINHA CONTROLLER:

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	
	// FIXTURES
	@BeforeEach
	void setUp() throws Exception{
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		
		//RETURN => quando(when)--ação(thenReturn)
		Mockito.when(service.findALLPaged(ArgumentMatchers.any())).thenReturn(page);
		
		
	}
	
	//---TESTE PARA VALIDAR BUSCAS:
	@Test
	public void findAllShouldReturnPage()throws Exception{
		
		ResultActions result = mockMvc.perform(get("/products")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
}
