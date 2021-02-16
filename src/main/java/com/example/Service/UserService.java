package com.example.Service;

import com.example.Domain.User;
import com.example.Exceptions.EtAuthException;

public interface UserService {
	
	User validateUser(String email, String password) throws EtAuthException;
	
	User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;

}
