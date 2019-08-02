package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.appUser;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private
	BCryptPasswordEncoder bCryptPasswordEncoder;


	@GetMapping("/id/{id}")
	public ResponseEntity<appUser> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<appUser> findByUserName(@PathVariable String username) {
		appUser user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<appUser> createUser(@RequestBody CreateUserRequest createUserRequest) {
		appUser user = new appUser();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		if (createUserRequest.getPassword().length() < 5 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmedPassword())) {
			return ResponseEntity.badRequest().build();
		}
		log.info("User name was set with this value", user.getUsername());
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
}
