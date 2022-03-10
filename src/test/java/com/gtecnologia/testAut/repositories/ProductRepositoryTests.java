package com.gtecnologia.testAut.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.gtecnologia.testAut.entities.Product;
import com.gtecnologia.testAut.factory.Factory;
import com.gtecnologia.testAut.services.exceptions.ResourceNotFoundException;

//TESTES||COMPONENTES RELACIONADOS AO SPRING DATA JPA||VALIDAR METODOS DO MEU REPOSITÓRIO:
@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	// FIXTURES

	private long exintingId;
	private long nonExistingId;
	private long countTotalProduct;

	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		nonExistingId = 100L;
		countTotalProduct = 25L;

	}

	//---TESTE PARA VALIDAR BUSCAS:
	@Test
	public void findByIdShoulReturnOptionalNotNullWhenIdExist() {

		Optional<Product> result = repository.findById(exintingId);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShoulReturnOptionalNullWhenIdNoExist() {

		Optional<Product> result = repository.findById(nonExistingId);

		Assertions.assertFalse(result.isPresent());
		Assertions.assertTrue(result.isEmpty());
	}

    //---TESTES PARA VALIDAR INSERÇÃO E ATUALIZAÇÃO:
	@Test // SAVE DEVE SALVAR E OBJ E AUTOINCREMENTAR "ID" QND ID FOR NULO. EX:(insert)
	public void saveShoulPersistWithAutoIncrementWhenIdIsNull() {

		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProduct + 1, product.getId());
	}

	//TESTES PARA VALIDAR DELEÇÃO:
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		repository.deleteById(exintingId);
		Optional<Product> result = repository.findById(exintingId);

		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccesExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}

}
