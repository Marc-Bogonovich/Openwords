package com.openwords.view;

class UserValidLetter {
	//This class is only for validation
	//Small size to reduce transfer time
	//If necessary, could apply encryption on it.
	private String username;
	private String password;
	
	public UserValidLetter(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
