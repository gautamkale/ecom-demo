package com.rest.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.ecommerce.models.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {

}