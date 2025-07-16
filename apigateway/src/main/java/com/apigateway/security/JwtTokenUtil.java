package com.apigateway.security;

import java.util.Date;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import com.apigateway.config.JwtConfig;
import com.apigateway.exception.JwtTokenIncorrectStructureException;
import com.apigateway.exception.JwtTokenMalformedException;
import com.apigateway.exception.JwtTokenMissingException;

@Component
public class JwtTokenUtil {

	private final JwtConfig config;

	public JwtTokenUtil(JwtConfig config) {
		this.config = config;
	}

	public String generateToken(String id) {
		Claims claims = Jwts.claims().setSubject(id);
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + config.getValidity() * 1000 * 60;
		Date exp = new Date(expMillis);
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, config.getSecret()).compact();
	}
	
	public void validateToken(final String header)
	        throws JwtTokenMalformedException, JwtTokenMissingException {
	    if (header == null || header.trim().isEmpty()) {
	        throw new JwtTokenMissingException("Authorization header is missing");
	    }

	    String[] parts = header.split(" ");
	    if (parts.length != 2 || !"Bearer".equals(parts[0])) {
	        throw new JwtTokenIncorrectStructureException("Incorrect Authentication Structure");
	    }

	    try {
	        Jwts.parser()
	            .setSigningKey(config.getSecret())
	            .parseClaimsJws(parts[1]);
	    } catch (SignatureException | MalformedJwtException | UnsupportedJwtException ex) {
	        throw new JwtTokenMalformedException("Invalid JWT token: " + ex.getMessage());
	    } catch (ExpiredJwtException ex) {
	        throw new JwtTokenMalformedException("Expired JWT token");
	    } catch (IllegalArgumentException ex) {
	        throw new JwtTokenMissingException("JWT claims string is empty.");
	    }
	}


//	public void validateToken(final String header) throws JwtTokenMalformedException, JwtTokenMissingException {
//		try {
//			String[] parts = header.split(" ");
//			if (parts.length != 2 || !"Bearer".equals(parts[0])) {
//				throw new JwtTokenIncorrectStructureException("Incorrect Authentication Structure");
//			}
//
//			Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(parts[1]);
//		} catch (SignatureException ex) {
//			throw new JwtTokenMalformedException("Invalid JWT signature");
//		} catch (MalformedJwtException ex) {
//			throw new JwtTokenMalformedException("Invalid JWT token");
//		} catch (ExpiredJwtException ex) {
//			throw new JwtTokenMalformedException("Expired JWT token");
//		} catch (UnsupportedJwtException ex) {
//			throw new JwtTokenMalformedException("Unsupported JWT token");
//		} catch (IllegalArgumentException ex) {
//			throw new JwtTokenMissingException("JWT claims string is empty.");
//		}
//	}
}
