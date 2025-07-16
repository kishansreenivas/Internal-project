package com.apigateway.controller;

import com.apigateway.models.AuthenticationStatus;
import com.apigateway.models.ErrorResponseDto;
import com.apigateway.models.JwtRequest;
import com.apigateway.models.JwtResponse;
import com.apigateway.security.JwtTokenUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.apigateway.constants.Constants.API_GATEWAY_PREDICATE;

@RestController

public class JwtAuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;
       
   
    public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

      @RequestMapping(value = API_GATEWAY_PREDICATE + "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        String username = authenticationRequest.getUsername().trim().toLowerCase();
        String password = authenticationRequest.getPassword();

        if (!isUsernameInFile(username)) {
            List<String> details = new ArrayList<>();
            details.add("Username not found in system.");
            ErrorResponseDto error = new ErrorResponseDto(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", details, "uri");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        AuthenticationStatus status = authenticate(username, password);

        if (!status.getIsAuthenticated()) {
            List<String> details = new ArrayList<>();
            System.out.println("User is not getting getIsAuthenticated");
            details.add(status.getMessage());
            ErrorResponseDto error = new ErrorResponseDto(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", details, "uri");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        final String token = jwtTokenUtil.generateToken(username);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private boolean isUsernameInFile(String username) {
        String filePath = "C:\\Documents\\Simple\\SpringBoot\\File\\createuser.txt";
        		
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.toLowerCase().startsWith("username:")) {
                    String fileUsername = line.substring("username:".length()).trim().toLowerCase();
                    if (fileUsername.equals(username)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // In production, use a logger
        }
        return false;
    }

    private AuthenticationStatus authenticate(String username, String password) {
        // Skip the password check and always return successful authentication
        return new AuthenticationStatus(true, "Authentication Successful");
    }
}
	/**
	 * * *** NOTE: ***
	 * * Api Gateway should match predicate
	 * * path to be discoverable in swagger
	 */
//	@RequestMapping(value = API_GATEWAY_PREDICATE + "/authenticate", method = RequestMethod.POST)
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
//		AuthenticationStatus status = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//		if (!status.getIsAuthenticated()) {
//			List<String> details = new ArrayList<>();
//			details.add(status.getMessage());
//			ErrorResponseDto error = new ErrorResponseDto(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", details, "uri");
//			return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//		}
//
//		final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
//		return ResponseEntity.ok(new JwtResponse(token));
//	}
//
//	private AuthenticationStatus authenticate(String username, String password) {
//		AuthenticationStatus status;
//
//		if (!username.equals("foo") && !password.equals("foo")) {
//			status = new AuthenticationStatus(false, "Invalid Username/Password");
//		}
//		else {
//			status = new AuthenticationStatus(true, "Authentication Successful");
//		}
//
//		return status;
//	}
