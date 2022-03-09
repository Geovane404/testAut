package com.gtecnologia.testAut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gtecnologia.testAut.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
