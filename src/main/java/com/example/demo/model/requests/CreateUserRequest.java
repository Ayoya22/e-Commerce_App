package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

	@JsonProperty
	private String username;
	private String password;
	private String confirmedPassword;

	public String getUsername() {
		return username;
	}
	public String getPassword() { return password; }
	public String getConfirmedPassword() {return confirmedPassword;}


	public void setPassword(String password) { this.password = password; }

	public void setConfirmedPassword(String confirmedPassword) {this.confirmedPassword = confirmedPassword;}

	public void setUsername(String username) { this.username = username; }


}
