package com.gtecnologia.testAut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gtecnologia.testAut.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
