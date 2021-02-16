package com.example.Repository;

import com.example.Domain.User;
import com.example.Exceptions.EtAuthException;

public interface UserRepository {

	Integer create(String firstName,String lastName, String email, String password) throws EtAuthException;
	
	User findByEmailAndPassword(String email, String password) throws EtAuthException;
	
	Integer getCountByEmail(String email) throws EtAuthException;
	
	User findById(Integer userId) throws EtAuthException;
	
	
}