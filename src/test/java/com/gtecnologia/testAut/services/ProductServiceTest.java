package com.gtecnologia.testAut.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gtecnologia.testAut.repositories.ProductRepository;
import com.gtecnologia.testAut.services.exceptions.DatabaseException;
import com.gtecnologia.testAut.services.exceptions.ResourceNotFoundException;

//TESTES || UNITARIO COM MOCKITO || VALIDAR METODOS DA MINHA CLASSE SERVICE:

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	// FIXTURES
	private long exintingId;
	private long dependentID;
	private long nonExintingId;

	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		dependentID = 2L;
		nonExintingId = 3L;

		// CONFIGURAR COMPORTAMENTO SIMULADO DO MOCK-(REPOSITORY) AO SER ACESSADO PELO "SERVICE":
		// 1°-RETURN => quando(when)--ação(thenReturn)

		// 2°-VOID => ação -- quando
		Mockito.doNothing().when(repository).deleteById(exintingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExintingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentID);

	}

	// TESTES PARA VALIDAR DELEÇÕES:
	@Test
	public void deleteShouldDoNothingWhenIdexist() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(exintingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(exintingId);
	}
	
	@Test
	public void deleteThrowResourceNotFoundExceptionWhenIdNoExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExintingId);
		});
		
		verify(repository).deleteById(nonExintingId);
	}
	
	@Test
	public void deleteThrowDataIntegrityViolationExceptionWhenDependentId() {
		 
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentID);
		});
		
		verify(repository).deleteById(dependentID);
	}

}







