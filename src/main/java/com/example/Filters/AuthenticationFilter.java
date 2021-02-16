package com.example.Filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.example.ApartmentManagment.Constants.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String authHeader = httpRequest.getHeader("Authorization");
        if(authHeader != null) {
            String[] authHeaderArr = authHeader.split("Apartment ");
            if(authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();
                    httpRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
                }catch (Exception e) {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
                    return;
                }
            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Apartment [token]");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided");
            return;
        }
        chain.doFilter(request, response);
    }
		
}

