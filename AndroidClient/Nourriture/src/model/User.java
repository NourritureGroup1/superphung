package model;

public class User {
	private String email;
	private String name;
	private String password;
	private String role;
	private boolean connected = false;
	
	public User(String name_, String role_,
			String email_, String password_) {
		name = name_;
		email = email_;
		role = role_;
		password = password_;
	}

	public void setRole(String role_)
	{
		role = role_;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public void setEmail(String email_) 
	{
		email = email_;
	}
	
	public String getEmail() 
	{
		return email;
	}
	
	public void setName(String name_)
	{
		name = name_;		
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setPassword(String password_)
	{
		password = password_;
	}
	
	public String getPassword()
	{
		return password;
	}

	public void setConnected(boolean state)
	{
		connected = state;
	}

	public boolean getConnected()
	{
		return connected;
	}
}