package com.example.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.Domain.User;
import com.example.Exceptions.EtAuthException;

@Repository
@SuppressWarnings("deprecation")
public class UserRepositoryImpl implements UserRepository {
	
	private static String create = "INSERT INTO AM_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES(NEXTVAL('AM_USERS_SEQ'),?,?,?,?)";

	private static String countByEmail = "SELECT COUNT(*) FROM AM_USERS WHERE EMAIL=? ";
	
	private static String findById = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM AM_USERS WHERE USER_ID = ?";
	
	private static String findByEmailIdAndPassword = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM AM_USERS WHERE EMAIL = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	@Override
	public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(16));
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				 PreparedStatement ps = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS);
	                ps.setString(1, firstName);
	                ps.setString(2, lastName);
	                ps.setString(3, email);
	                ps.setString(4, hashedPassword);
	                return ps;
	            }, keyHolder);
	            return (Integer) keyHolder.getKeys().get("USER_ID");
		}catch(EtAuthException e) {
			throw new EtAuthException("Invalid Details");
		}
		
	}

	@Override
	public User findByEmailAndPassword(String email, String password) throws EtAuthException {
		try {
			User user = jdbcTemplate.queryForObject(findByEmailIdAndPassword, new Object[] {email}, userRowMapper);
			if(user.getPassword() != null) {
				if(!BCrypt.checkpw(password, user.getPassword())) {
					throw new EtAuthException("Invalid email/password");
				}
			}else {
				throw new EtAuthException("Enter Valid Password");
			}
			return user;
		}catch (Exception e) {
			throw new EtAuthException("Invalid email/password");
		}
	}

	
	
	@Override
	public Integer getCountByEmail(String email) {
		return jdbcTemplate.queryForObject(countByEmail, new Object[] {email}, Integer.class);
	}
		

	@Override
	public User findById(Integer userId)  {	
		return jdbcTemplate.queryForObject(findById, new Object[]{userId}, userRowMapper);
	}
	
	private RowMapper<User> userRowMapper = ((rs ,rowNum) -> {
		return new User(rs.getInt("USER_ID"),
				rs.getString("FIRST_NAME"),
				rs.getString("LAST_NAME"),
				rs.getString("EMAIL"),
				rs.getString("PASSWORD"));
	});

}
