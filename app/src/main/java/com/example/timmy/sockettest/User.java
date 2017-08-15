package com.example.timmy.sockettest;

public class User {

	public String id;
	public String username ; 
	public String password  ;
	public String sex;
	public String telphone;
	public String email;

	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String id,String username, String password,String sex,String telphone,String email) {
		this.id=id;
		this.username = username;
		this.password = password;
		this.sex=sex;
		this.telphone=telphone;
		this.email=email;
	}

	@Override
	public String toString()
	{
		return "User{" +
				"id='" + id + '\'' +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", sex='" + sex + '\'' +
				", telphone='" + telphone + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
