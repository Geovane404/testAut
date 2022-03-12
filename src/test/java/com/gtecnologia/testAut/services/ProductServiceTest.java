package com.gtecnologia.testAut.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gtecnologia.testAut.dto.ProductDTO;
import com.gtecnologia.testAut.entities.Category;
import com.gtecnologia.testAut.entities.Product;
import com.gtecnologia.testAut.factory.Factory;
import com.gtecnologia.testAut.repositories.CategoryRepository;
import com.gtecnologia.testAut.repositories.ProductRepository;
import com.gtecnologia.testAut.services.exceptions.DatabaseException;
import com.gtecnologia.testAut.services.exceptions.ResourceNotFoundException;

//TESTES || UNITARIO COM MOCKITO - MOCK || VALIDAR METODOS DA MINHA CLASSE SERVICE:

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	
	private long exintingId;
	private long dependentID;
	private long nonExintingId;

	private ProductDTO productDTO;
	private Product product;
	private Category category;
	private PageImpl<Product> page;

	// FIXTURES
	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		dependentID = 2L;
		nonExintingId = 3L;

		productDTO = Factory.createProductDTO();
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));

		// CONFIGURAR COMPORTAMENTO SIMULADO DO MOCK-(REPOSITORY) AO SER ACESSADO PELO "SERVICE":
		// 1°-RETURN => quando(when)--ação(thenReturn)
		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.findById(exintingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExintingId)).thenReturn(Optional.empty());

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.getOne(exintingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExintingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(exintingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExintingId)).thenThrow(EntityNotFoundException.class);

		// 2°-VOID => ação -- quando
		Mockito.doNothing().when(repository).deleteById(exintingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExintingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentID);

	}

	// ---TESTES PARA VALIDAR BUSCAS:
	@Test
	public void findAllPagedShoulReturnPage() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findALLPaged(pageable);

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());

		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {

		ProductDTO result = service.findById(exintingId);

		Assertions.assertNotNull(result);
		Mockito.verify(repository).findById(exintingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNoExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExintingId);
		});

		Mockito.verify(repository).findById(nonExintingId);
	}

	// ---TESTES PARA VALIDAR INSERÇÕES E ATUALIZAÇÕES:
	@Test
	public void insertShouldReturnProductDTO() {

		ProductDTO dto = service.insert(productDTO);

		Assertions.assertNotNull(dto);
	}

	@Test
	public void updateShoulReturnProductDTOWhenIdExist() {

		ProductDTO dto = service.update(exintingId, productDTO);

	    Assertions.assertEquals(productDTO.getName(), dto.getName());
		Assertions.assertNotNull(dto);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdNoExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ProductDTO dto = service.update(nonExintingId, productDTO);
		});
	}
	
	// ---TESTES PARA VALIDAR DELEÇÕES:
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
