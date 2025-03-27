package com.example.springwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springwork.entity.Manual;

public interface ManualRepository extends JpaRepository<Manual, Integer>{
	public List<Manual> findByNameOrderByCreatedAtDesc(String name);
	
	public Page<Manual> findByNameLike(String keyword, Pageable pageable);
}
