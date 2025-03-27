package com.example.springwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springwork.entity.Summarize;

public interface SummarizeRepository extends JpaRepository<Summarize, Integer>{
	public List<Summarize> findByNameOrderByCreatedAtDesc(String name);
	
	public Page<Summarize> findByNameLike(String keyword, Pageable pageable);
}
