package com.example.springwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springwork.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	public List<Product> findByNameOrderByCreatedAtDesc(String name);
	
	public Page<Product> findByNameLike(String keyword, Pageable pageable);
	
}
