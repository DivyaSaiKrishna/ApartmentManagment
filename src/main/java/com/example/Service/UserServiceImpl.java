package com.example.Service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Domain.User;
import com.example.Exceptions.EtAuthException;
import com.example.Repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	 @Autowired
	 UserRepository userRepository;

	@Override
	public User validateUser(String email, String password) throws EtAuthException {
		if(email != null) {
			email = email.toLowerCase();
		}
		return userRepository.findByEmailAndPassword(email, password);
	}

	@Override
	public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		if(email != null) email = email.toLowerCase();
		if(!pattern.matcher(email).matches()) {
			throw new EtAuthException("Entered Email is Invaild");
		}
		if(userRepository.getCountByEmail(email)>0) {
			throw new EtAuthException("Email is already in use");
		}
		int userId = userRepository.create(firstName, lastName, email, password);
		return userRepository.findById(userId);
	}

}
