package com.example.springwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springwork.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer>{
	public Role findByName(String name);
}
