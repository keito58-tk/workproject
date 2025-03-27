package com.example.springwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springwork.entity.User;



public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByName(String name);
}
